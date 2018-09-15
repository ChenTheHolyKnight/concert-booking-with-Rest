package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.ConcertDTO;
import nz.ac.auckland.concert.service.domain.model.Concert;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import nz.ac.auckland.concert.service.services.mapper.ConcertMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nz.ac.auckland.concert.common.Config.ALL_CONCERTS;
import static nz.ac.auckland.concert.common.Config.CONCERTS_URI;
import static nz.ac.auckland.concert.common.Config.COOKIE;


@Path(CONCERTS_URI)
public class ConcertResource extends ServiceResource{
    private static Logger _logger = LoggerFactory
            .getLogger(ConcertResource.class);

    // Declare necessary instance variables.
    private PersistenceManager _persistenceManager;

    public ConcertResource(){
        _persistenceManager=PersistenceManager.instance();
    }


    @GET
    @Path(ALL_CONCERTS)
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response retrieveAllConcert(@CookieParam(COOKIE) Cookie clientId) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        List<Concert> concerts=entityManager.createQuery("SELECT c FROM Concert c",Concert.class).getResultList();
        Set<ConcertDTO> concertDTOS=new HashSet<>();
        concerts.forEach(concert -> concertDTOS.add(ConcertMapper.toDTO(concert)));
        entityManager.getTransaction().commit();
        entityManager.close();
        if(!concertDTOS.isEmpty()){
            GenericEntity<Set<ConcertDTO>> entity = new GenericEntity<Set<ConcertDTO>>(concertDTOS) {};
            return Response.ok(entity).cookie(makeCookie(clientId)).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
