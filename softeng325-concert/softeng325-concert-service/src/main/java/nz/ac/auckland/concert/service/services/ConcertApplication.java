package nz.ac.auckland.concert.service.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
@ApplicationPath("/services")
public class ConcertApplication extends Application{
    private Set<Object> singleton =new HashSet<>();
    private Set<Class<?>> classes=new HashSet<>();
    public ConcertApplication() {
        classes.add(ConcertResource.class);
        classes.add(PerformerResource.class);
        singleton.add(PersistenceManager.instance());
    }

    @Override
    public Set<Object> getSingletons(){
        return singleton;
    }

    @Override
    public Set<Class<?>> getClasses(){
        return classes;
    }
}
