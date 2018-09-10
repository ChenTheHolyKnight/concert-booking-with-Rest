package nz.ac.auckland.concert.service.services;

import nz.ac.auckland.concert.common.dto.ConcertDTO;
import nz.ac.auckland.concert.service.domain.model.Concert;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


@Path("/concert")
public class ConcertResource {
    private static Logger _logger = LoggerFactory
            .getLogger(ConcertResource.class);

    // Declare necessary instance variables.
    private PersistenceManager _persistenceManager;

    public ConcertResource(){
        _persistenceManager=PersistenceManager.instance();
    }


    /**
     * Retrieves a Concert based on its unique id. The HTTP response message
     * has a status code of either 200 or 404, depending on whether the
     * specified Concert is found.
     *
     * When clientId is null, the HTTP request message doesn't contain a cookie
     * named clientId (Config.CLIENT_COOKIE), this method generates a new
     * cookie, whose value is a randomly generated UUID. This method returns
     * the new cookie as part of the HTTP response message.
     *
     * This method maps to the URI pattern <base-uri>/concerts/{id}.
     *
     * @param id the unique ID of the Concert.
     *
     * @p  a cookie named Config.CLIENT_COOKIE that may be sent
     * by the client.
     *
     * @return a Response object containing the required Concert.
     */

    @GET
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public Response retrieveConcert(@PathParam("id") long id) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        Concert concert=entityManager.find(Concert.class,id);
        entityManager.getTransaction().commit();



        if(concert!=null){
            GenericEntity<Concert> entity = new GenericEntity<Concert>(concert) {};
            return Response.ok(entity).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public Response retrieveAllConcert(@PathParam("id") long id) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        List<Concert> concerts=entityManager.createQuery("select c from CONCERT c",Concert.class).getResultList();
        Set<ConcertDTO> concertDTOS=new HashSet<>();
        concerts.forEach(concert -> concertDTOS.add(ConcertMapper.toDTO(concert)));
        entityManager.getTransaction().commit();


        if(!concertDTOS.isEmpty()){
            GenericEntity<Set<ConcertDTO>> entity = new GenericEntity<Set<ConcertDTO>>(concertDTOS) {};
            return Response.ok(entity).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }




    /**
     * Creates a new Concert. This method assigns an ID to the new Concert and
     * stores it in memory. The HTTP Response message returns a Location header
     * with the URI of the new Concert and a status code of 201.
     *
     * When clientId is null, the HTTP request message doesn't contain a cookie
     * named clientId (Config.CLIENT_COOKIE), this method generates a new
     * cookie, whose value is a randomly generated UUID. This method returns
     * the new cookie as part of the HTTP response message.
     *
     * This method maps to the URI pattern <base-uri>/concerts.
     *
     * @param concert the new Concert to create.
     *
     * @pa clientId a cookie named Config.CLIENT_COOKIE that may be sent
     * by the client.
     *
     * @return a Response object containing the status code 201 and a Location
     * header.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public Response createConcert(Concert concert) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(concert);
        entityManager.getTransaction().commit();

        return Response.created(URI.create("/concerts/"+concert.getId())).status(Response.Status.CREATED).build();
    }


    /**
     * Deletes all Concerts, returning a status code of 204.
     *
     * When clientId is null, the HTTP request message doesn't contain a cookie
     * named clientId (Config.CLIENT_COOKIE), this method generates a new
     * cookie, whose value is a randomly generated UUID. This method returns
     * the new cookie as part of the HTTP response message.
     *
     * This method maps to the URI pattern <base-uri>/concerts.
     *
     * @param clientId a cookie named Config.CLIENT_COOKIE that may be sent
     * by the client.
     *
     * @return a Response object containing the status code 204.
     */
    @DELETE
    public Response deleteAllConcerts(@CookieParam("clientId") Cookie clientId) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.clear();
        entityManager.getTransaction().commit();
        entityManager.close();

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    @Path("{id}")
    public Response deleteConcert(@PathParam("id") long id) {
        EntityManager entityManager=_persistenceManager.createEntityManager();
        entityManager.getTransaction().begin();
        Concert concert=entityManager.find(Concert.class,id);
        entityManager.remove(concert);
        entityManager.getTransaction().commit();
        entityManager.close();


        return Response.status(Response.Status.NO_CONTENT).build();
    }





}
