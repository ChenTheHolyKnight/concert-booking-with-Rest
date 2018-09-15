package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.CreditCardDTO;
import nz.ac.auckland.concert.common.message.Messages;
import nz.ac.auckland.concert.service.domain.model.CreditCard;
import nz.ac.auckland.concert.service.domain.model.User;
import nz.ac.auckland.concert.service.services.PersistenceManager;
import nz.ac.auckland.concert.service.services.mapper.CreditCardMapper;

import javax.persistence.EntityManager;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.List;

import static nz.ac.auckland.concert.common.Config.*;

@Path(CREDITCARD_URI)
public class CreditCardResource extends ServiceResource{
    private PersistenceManager _persistenceManager;

    public CreditCardResource(){
        _persistenceManager=PersistenceManager.instance();
    }

    @POST
    @Path(REGISTER_CREDITCARD)
    public Response registerCreditCard(CreditCardDTO creditCardDTO, @CookieParam(COOKIE) Cookie clientId){
        if(clientId==null){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.UNAUTHENTICATED_REQUEST).build();
        }
        String uuid=clientId.getValue();
        EntityManager entityManager=_persistenceManager.createEntityManager();
        List<User> users=entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
        if(users.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.BAD_AUTHENTICATON_TOKEN).build();
        }

        User user=users.get(0);
        CreditCard creditCard= CreditCardMapper.ToDomain(creditCardDTO,user);
        entityManager.persist(creditCard);

        return Response.status(Response.Status.OK).cookie(makeCookie(clientId)).build();
    }
}
