package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.ReservationRequestDTO;
import nz.ac.auckland.concert.common.message.Messages;
import nz.ac.auckland.concert.common.types.PriceBand;
import nz.ac.auckland.concert.service.domain.model.*;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

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
        User user=users.get(0);

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
                .setMaxResults(requestAmount)
                .getResultList();

        if(availableSeats.size()!=requestAmount){
            return Response.status(Response.Status.BAD_REQUEST).entity(Messages.INSUFFICIENT_SEATS_AVAILABLE_FOR_RESERVATION).build();
        }





        return Response.status(Response.Status.OK).cookie(makeCookie(clientId)).build();

    }

    private Reservation convertReservation(ReservationRequestDTO reservationRequestDTO, User user){
        return null;
    }

    private ReservationRequest convertReservationRequest(ReservationRequestDTO reservationRequestDTO){
        Long id=reservationRequestDTO.getConcertId();
        List<Concert> concerts=_entityManager.createQuery("select c from Concert c where _ID =\'"+id+"\'").getResultList();
        Concert concert = null;
        if(!concerts.isEmpty()){
            concert=concerts.get(0);
        }

        return new ReservationRequest(
                reservationRequestDTO.getNumberOfSeats(),
                reservationRequestDTO.getSeatType(),
                concert,
                reservationRequestDTO.getDate()
        );
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




}
