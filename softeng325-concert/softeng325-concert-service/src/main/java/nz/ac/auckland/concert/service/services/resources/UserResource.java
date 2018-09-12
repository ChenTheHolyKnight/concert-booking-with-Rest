package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.UserDTO;
import nz.ac.auckland.concert.common.message.Messages;
import nz.ac.auckland.concert.service.domain.model.User;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import nz.ac.auckland.concert.service.services.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static nz.ac.auckland.concert.common.Config.CREATE_USER;
import static nz.ac.auckland.concert.common.Config.USER_URI;

@Path(USER_URI)
public class UserResource extends ServiceResource {
    //private final static String USER_URI="/create";
    private PersistenceManager _persistenceManager;
    EntityManager _entityManager;


    private static Logger _logger = LoggerFactory
            .getLogger(ConcertResource.class);

    public UserResource(){
        _persistenceManager=PersistenceManager.instance();
        _entityManager=_persistenceManager.createEntityManager();
    }
    @POST
    @Path(CREATE_USER)
    @Produces({MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_XML})
    public Response createUser(UserDTO userDTO, @CookieParam(COOKIE) Cookie cookie) {
        if(!checkEmptyField(userDTO)){
            return Response.status(Response.Status.NOT_FOUND).entity(Messages.CREATE_USER_WITH_MISSING_FIELDS).build();
        }
        if(checkUserNameExists(userDTO)){
            return Response.status(Response.Status.NOT_FOUND).entity(Messages.CREATE_USER_WITH_NON_UNIQUE_NAME).build();
        }

        User user = UserMapper.ToUser(userDTO);
        _entityManager.persist(user);
        _entityManager.getTransaction().commit();

        NewCookie newCookie=makeCookie(cookie);

        return Response.created(URI.create(CREATE_USER+"/"+user.getUsername())).entity(userDTO).cookie(newCookie).build();

    }

    private boolean checkUserNameExists(UserDTO userDTO){

        _entityManager.getTransaction().begin();
        List<User> users=_entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        boolean isExists=false;
        for(User e: users) {
            String username = e.getUsername();
            String user = userDTO.getUsername();
            if (user == username) {
                isExists = true;
            }
        }
        return isExists;


    }

    private boolean checkEmptyField(UserDTO userDTO){
        if(userDTO.getFirstname()==null){
            return false;
        }
        if(userDTO.getLastname()==null){
            return false;
        }
        if(userDTO.getPassword()==null){
            return false;
        }
        if (userDTO.getUsername()==null){
            return false;
        }

        return true;
    }
}