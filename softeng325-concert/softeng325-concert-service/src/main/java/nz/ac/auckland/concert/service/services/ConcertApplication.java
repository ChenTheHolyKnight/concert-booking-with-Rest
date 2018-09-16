package nz.ac.auckland.concert.service.services;

import nz.ac.auckland.concert.common.types.PriceBand;
import nz.ac.auckland.concert.common.types.SeatNumber;
import nz.ac.auckland.concert.common.types.SeatRow;
import nz.ac.auckland.concert.service.domain.model.Concert;
import nz.ac.auckland.concert.service.domain.model.CreditCard;
import nz.ac.auckland.concert.service.domain.model.Seat;
import nz.ac.auckland.concert.service.services.resources.*;
import nz.ac.auckland.concert.utility.TheatreLayout;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlTypeProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationPath("/services")
public class ConcertApplication extends Application {
    private Set<Object> singleton = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public ConcertApplication() {


        PersistenceManager persistenceManager = PersistenceManager.instance();


        EntityManager em = persistenceManager.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM BOOKING_SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM BOOKING").executeUpdate();
        em.createNativeQuery("DELETE FROM CREDITCARD").executeUpdate();
        em.createNativeQuery("DELETE FROM RESERVATION_SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM RESERVATION").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        generateSeats(em);
        em.flush();
        em.clear();
        em.getTransaction().commit();


        classes.add(ConcertResource.class);
        classes.add(PerformerResource.class);
        classes.add(UserResource.class);
        classes.add(CreditCardResource.class);
        classes.add(ReservationResource.class);
        singleton.add(persistenceManager);
        em.close();
    }

    @Override
    public Set<Object> getSingletons() {
        return singleton;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    private void generateSeats(EntityManager entityManager){

        List<Timestamp> dates=entityManager.createNativeQuery("select C._DATES from CONCERT_DATES c").getResultList();

        Set<SeatRow> rowA=TheatreLayout.getRowsForPriceBand(PriceBand.PriceBandA);
        Set<SeatRow> rowB=TheatreLayout.getRowsForPriceBand(PriceBand.PriceBandB);
        Set<SeatRow> rowC=TheatreLayout.getRowsForPriceBand(PriceBand.PriceBandC);
        for(int i=0;i<dates.size();i++){
            LocalDateTime dateTime=dates.get(i).toLocalDateTime();
            constructThreatre(rowA,PriceBand.PriceBandA,dateTime,entityManager);
            constructThreatre(rowB,PriceBand.PriceBandB,dateTime,entityManager);
            constructThreatre(rowC,PriceBand.PriceBandC,dateTime,entityManager);
            entityManager.flush();
            entityManager.clear();
        }

    }

    private void constructThreatre(Set<SeatRow> row,PriceBand pB,LocalDateTime dateTime,EntityManager entityManager){
        row.forEach(seatRow -> {
            int maxRow=TheatreLayout.getNumberOfSeatsForRow(seatRow);
            for(int i=1;i<maxRow+1;i++){
                Seat seat=new Seat(
                  seatRow,
                  new SeatNumber(i),
                  pB,
                  null, dateTime
                );
                entityManager.persist(seat);
            }
            entityManager.flush();
            entityManager.clear();
        });

    }
}
