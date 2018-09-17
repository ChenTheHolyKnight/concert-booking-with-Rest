package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.NewsDTO;
import nz.ac.auckland.concert.service.services.PersistenceManager;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static nz.ac.auckland.concert.common.Config.SUBSCRIBE;

@Path(SUBSCRIBE)
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class SubscribeResource extends ServiceResource{
    //To create a singleton object
    private static SubscribeResource _instance = null;
    private PersistenceManager _persistenceManager=PersistenceManager.instance();
    private EntityManager _entityManager;

    private SubscribeResource() {
        _entityManager=_persistenceManager.createEntityManager();
    }

    public static SubscribeResource instance() {
        if (_instance == null) {
            _instance = new SubscribeResource();
        }
        return _instance;
    }

    private List<AsyncResponse> _responses = new ArrayList<AsyncResponse>( );

    @GET
    public Response subscribe(@Suspended AsyncResponse response) {
        try{
            _responses.add(response);

            return Response.status(Response.Status.OK).build();

        }catch (Exception e){
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

    }

    @POST
    public void send(NewsDTO message)
    {
        _responses.forEach(response->response.resume(message));
    }



}
