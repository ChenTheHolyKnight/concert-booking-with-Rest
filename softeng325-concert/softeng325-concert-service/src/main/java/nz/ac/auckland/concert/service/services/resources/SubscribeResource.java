package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.message.Messages;
import nz.ac.auckland.concert.service.domain.model.News;
import nz.ac.auckland.concert.service.domain.model.User;
import nz.ac.auckland.concert.service.services.PersistenceManager;

import javax.persistence.EntityManager;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nz.ac.auckland.concert.common.Config.*;

@Path(SUBSCRIBE)
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

    private HashMap<AsyncResponse,Cookie> _responsesMap = new HashMap<>();
    private Map<Cookie,AsyncResponse> _cookieMap=new HashMap<>();

    @GET
    @Path(MAKESUBSCRIBE)
    public Response subscribe(@Suspended AsyncResponse response, @CookieParam(COOKIE) Cookie clientId) {
        try{
            if(clientId==null){
                return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.UNAUTHENTICATED_REQUEST).build();
            }
            String uuid=clientId.getValue();
            _entityManager.getTransaction().begin();
            List<User> users=_entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
            if(users.isEmpty()){
                return Response.status(Response.Status.UNAUTHORIZED).entity(Messages.BAD_AUTHENTICATON_TOKEN).build();
            }
            User user=users.get(0);

            List<News> news=_entityManager.createQuery("select n from News n").getResultList();

            boolean resume=false;
            for(News news1:news){
                if(news1.getId()==user.getNews().getId()){
                    resume=true;
                }
                if(resume){
                    response.resume(news1.getContent());
                }
            }

            _responsesMap.put(response,clientId);
            _cookieMap.put(clientId,response);

            return Response.status(Response.Status.OK).build();

        }catch (Exception e){
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

    }

    @POST
    @Path(SEND)
    public void send(String message)
    {
            List<AsyncResponse> responses=new ArrayList<>();
            responses.addAll(_cookieMap.values());
            responses.forEach(response->{
                try {
                    _entityManager.getTransaction().begin();
                    String uuid=_responsesMap.get(response).getValue();
                    List<User> users=_entityManager.createQuery("select u from User u where uuid =\'"+uuid+"\'").getResultList();
                    List<News> news=_entityManager.createQuery("select  n from News n where n._content=:content")
                            .setParameter("content",message)
                            .getResultList();
                    if(!users.isEmpty()){
                        User user=users.get(0);
                        News news1=news.get(0);
                        user.setNews(news1);
                        _entityManager.persist(news);
                        _entityManager.getTransaction().commit();
                    }
                }catch (Exception e){
                    Cookie cookie=_responsesMap.get(response);
                    _cookieMap.remove(cookie);
                    _responsesMap.remove(response);
                }

            });

    }



}
