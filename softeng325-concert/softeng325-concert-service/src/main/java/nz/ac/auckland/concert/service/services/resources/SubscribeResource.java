package nz.ac.auckland.concert.service.services.resources;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static nz.ac.auckland.concert.common.Config.COOKIE;
import static nz.ac.auckland.concert.common.Config.MAKESUBSCRIBE;
import static nz.ac.auckland.concert.common.Config.SUBSCRIBE;

@Path(SUBSCRIBE)
public class SubscribeResource extends ServiceResource{
    //To create a singleton object
    private static SubscribeResource _instance = null;

    private SubscribeResource() {
    }

    public static SubscribeResource instance() {
        if (_instance == null) {
            _instance = new SubscribeResource();
        }
        return _instance;
    }
    
    protected List<AsyncResponse> responses = new ArrayList<AsyncResponse>();

    @GET
    @Path(MAKESUBSCRIBE)
    public void subscribe(@Suspended AsyncResponse response, @CookieParam(COOKIE) Cookie clientId) {
        responses.add( response );
    }



}
