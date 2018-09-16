package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.*;
import nz.ac.auckland.concert.common.message.Messages;
import nz.ac.auckland.concert.common.types.PriceBand;
import nz.ac.auckland.concert.service.domain.model.*;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import nz.ac.auckland.concert.service.services.mapper.SeatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nz.ac.auckland.concert.common.Config.*;

@Path(RESERVATION_URI)
public class ReservationResource extends ServiceResource {
    private static Logger _logger = LoggerFactory
            .getLogger(ConcertResource.class);

    // Declare necessary instance variables.
    private PersistenceManager _persistenceManager;

    private EntityManager _entityManager;

    public ReservationResource(){
        _persistenceManager=PersistenceManager.instance();
    }

    @POST
    @Path(RESERVE_SEAT)
    public Response reserveSeats(ReservationRequestDTO reservationRequestDTO, @CookieParam(COOKIE) Cookie clientId){


        if(clientId==null){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.UNAUTHENTICATED_REQUEST).build();
        }
        String uuid=clientId.getValue();
        _entityManager=_persistenceManager.createEntityManager();
        _entityManager.getTransaction().begin();
        List<User> users=_entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
        if(users.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.BAD_AUTHENTICATON_TOKEN).build();
        }

        if(isIncompletion(reservationRequestDTO)){
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.RESERVATION_REQUEST_WITH_MISSING_FIELDS).build();
        }

        Long id=reservationRequestDTO.getConcertId();
        LocalDateTime date=reservationRequestDTO.getDate();
        List<Concert> concerts=_entityManager.createQuery("select c from Concert c Join c._dates d where _ID =:id and d=:date")
                .setParameter("id",id)
                .setParameter("date",date)
                .getResultList();
        if(concerts==null || concerts.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.CONCERT_NOT_SCHEDULED_ON_RESERVATION_DATE).build();
        }
        PriceBand type=reservationRequestDTO.getSeatType();
        int requestAmount=reservationRequestDTO.getNumberOfSeats();
        List<Seat>  availableSeats=_entityManager.createQuery("select s from Seat s where s._date=:date and s._seatType=:type and s._reservation is NULL")
                .setParameter("date",date)
                .setParameter("type",type)
                .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                .setMaxResults(requestAmount)
                .getResultList();

        if(availableSeats.size()!=requestAmount){
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.INSUFFICIENT_SEATS_AVAILABLE_FOR_RESERVATION).build();
        }
        Set<SeatDTO> seats=new HashSet<>();
        availableSeats.forEach(seat -> {
            seats.add(SeatMapper.toDTO(seat));
        });
        ReservationDTO reservationDTO=new ReservationDTO(
                reservationRequestDTO,
                seats
        );
        long expiry=System.currentTimeMillis()+5;
        ReservationRequest reservationRequest=new ReservationRequest(
              reservationRequestDTO.getNumberOfSeats(),
              reservationRequestDTO.getSeatType(),
              concerts.get(0),
              date
        );
        Set<Seat> seatSet=new HashSet<>();
        seatSet.addAll(availableSeats);
        Reservation reservation=new Reservation(
                reservationRequest,
                seatSet,
                users.get(0),
                expiry
        );
        reservation.assignReservationToSeat(reservation);
        _entityManager.persist(reservation);
        _entityManager.flush();
        _entityManager.getTransaction().commit();


        return Response.created(URI.create(RESERVE_SEAT+"/"+reservation)).cookie(makeCookie(clientId)).entity(reservationDTO).build();
    }


    private boolean isIncompletion(ReservationRequestDTO reservationRequestDTODTO){
        if(reservationRequestDTODTO.getSeatType()==null){
            return true;
        }
        if(reservationRequestDTODTO.getConcertId()==null){
            return true;
        }
        if(reservationRequestDTODTO.getDate()==null){
            return true;
        }
        return false;
    }


    @POST
    @Path(CONFIRM)
    public Response confirmReservation(ReservationDTO reservation,@CookieParam(COOKIE) Cookie clientId) {
        if(clientId==null){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.UNAUTHENTICATED_REQUEST).build();
        }
        String uuid=clientId.getValue();
        _entityManager=_persistenceManager.createEntityManager();
        _entityManager.getTransaction().begin();
        List<User> users=_entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
        if(users.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.BAD_AUTHENTICATON_TOKEN).build();
        }

        User user=users.get(0);
        if(user.getCreditCard()==null){
            Response response=Response.status(Response.Status.BAD_REQUEST).entity(Messages.CREDIT_CARD_NOT_REGISTERED).build();
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.CREDIT_CARD_NOT_REGISTERED).build();
        }



        return Response.status(Response.Status.OK).build();
    }
    @GET
    @Path(BOOKING)
    public Response getBookings(@CookieParam(COOKIE) Cookie clientId) {

        if(clientId==null){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.UNAUTHENTICATED_REQUEST).build();
        }
        String uuid=clientId.getValue();
        _entityManager=_persistenceManager.createEntityManager();
        _entityManager.getTransaction().begin();
        List<User> users=_entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
        if(users.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.BAD_AUTHENTICATON_TOKEN).build();
        }

        List<Reservation> reservations=_entityManager.createQuery("select r from Reservation r where r._user=:user and r._isConfirmed = true ")
                .setParameter("user",users.get(0))
                .getResultList();
        Set<BookingDTO> bookingDTOS=new HashSet<>();
        reservations.forEach(reservation -> {
            Set<SeatDTO> seatDTOS=new HashSet<>();
            Set<Seat> seats=reservation.getSeats();
            seats.forEach(seat -> seatDTOS.add(SeatMapper.toDTO(seat)));
            BookingDTO bookingDTO=new BookingDTO(
                    reservation.getReservationRequest().getConcert().getId(),
                    reservation.getReservationRequest().getConcert().getTitle(),
                    reservation.getReservationRequest().getDate(),
                    seatDTOS,
                    reservation.getReservationRequest().getSeatType()
            );
            bookingDTOS.add(bookingDTO);
        });
        _entityManager.flush();
        _entityManager.getTransaction().commit();

        GenericEntity<Set<BookingDTO>> entity = new GenericEntity<Set<BookingDTO>>(bookingDTOS) {};
        return Response.status(Response.Status.OK).entity(entity).cookie(makeCookie(clientId)).build();
    }
}
