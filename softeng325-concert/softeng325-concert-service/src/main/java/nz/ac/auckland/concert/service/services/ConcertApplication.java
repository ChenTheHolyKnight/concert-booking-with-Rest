package nz.ac.auckland.concert.service.services;

import nz.ac.auckland.concert.service.services.resources.ConcertResource;
import nz.ac.auckland.concert.service.services.resources.PerformerResource;
import nz.ac.auckland.concert.service.services.resources.UserResource;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlTypeProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class ConcertApplication extends Application {
    private Set<Object> singleton = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public ConcertApplication() {


        PersistenceManager persistenceManager = PersistenceManager.instance();


        EntityManager em = persistenceManager.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.createNativeQuery("DELETE FROM BOOKING_SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM BOOKING").executeUpdate();
        em.createNativeQuery("DELETE FROM CREDITCARD").executeUpdate();
        em.createNativeQuery("DELETE FROM RESERVATION_SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM SEAT").executeUpdate();
        em.createNativeQuery("DELETE FROM RESERVATION").executeUpdate();
        em.getTransaction().commit();


        classes.add(ConcertResource.class);
        classes.add(PerformerResource.class);
        classes.add(UserResource.class);
        singleton.add(persistenceManager);
    }

    @Override
    public Set<Object> getSingletons() {
        return singleton;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
