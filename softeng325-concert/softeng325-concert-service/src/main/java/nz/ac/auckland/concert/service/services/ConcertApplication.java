package nz.ac.auckland.concert.service.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
@ApplicationPath("/services")
public class ConcertApplication extends Application{
    private Set<Object> singleton =new HashSet<>();

    public ConcertApplication() {
        singleton.add(new ConcertResource());
    }

    @Override
    public Set<Object> getSingletons(){
        return singleton;
    }
}
