package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.PerformerDTO;
import nz.ac.auckland.concert.service.domain.model.Performer;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import nz.ac.auckland.concert.service.services.mapper.PerformerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nz.ac.auckland.concert.common.Config.ALL_PERFORMERS;
import static nz.ac.auckland.concert.common.Config.COOKIE;
import static nz.ac.auckland.concert.common.Config.PERFORMERS_URI;

@Path(PERFORMERS_URI)
public class PerformerResource extends ServiceResource{

    private static Logger _logger = LoggerFactory
            .getLogger(ConcertResource.class);

    // Declare necessary instance variables.
    private PersistenceManager _persistenceManager;

    public PerformerResource(){
        _persistenceManager=PersistenceManager.instance();
    }


    @GET
    @Path(ALL_PERFORMERS)
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response retrieveAllConcert(@CookieParam(COOKIE) Cookie clientId) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        List<Performer> performers=entityManager.createQuery("SELECT p FROM Performer p",Performer.class).getResultList();
        Set<PerformerDTO> performerDTOS=new HashSet<>();
        performers.forEach(performer -> performerDTOS.add(PerformerMapper.toDTO(performer)));
        entityManager.getTransaction().commit();

        if(!performerDTOS.isEmpty()){
            GenericEntity<Set<PerformerDTO>> entity = new GenericEntity<Set<PerformerDTO>>(performerDTOS) {};
            return Response.ok(entity).cookie(makeCookie(clientId)).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
