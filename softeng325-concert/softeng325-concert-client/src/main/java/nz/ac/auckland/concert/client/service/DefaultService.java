package nz.ac.auckland.concert.client.service;

import nz.ac.auckland.concert.common.Config;
import nz.ac.auckland.concert.common.dto.*;
import nz.ac.auckland.concert.common.message.Messages;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static nz.ac.auckland.concert.common.Config.*;

public class DefaultService implements ConcertService{

    Client client;

    private Response _response;

    private Set<String> _cookieValues=new HashSet<>();



    @Override
    public Set<ConcertDTO> getConcerts() throws ServiceException {
        Set<ConcertDTO> concertDTOS = null;
        try{
            client= ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI + CONCERTS_URI + ALL_CONCERTS).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
             _response=builder.get();
            if(_response.getStatus()==Response.Status.OK.getStatusCode()){
                concertDTOS=_response.readEntity(new GenericType<Set<ConcertDTO>>(){});
            }
        }catch (Exception e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
        return concertDTOS;
    }

    @Override
    public Set<PerformerDTO> getPerformers() throws ServiceException {
        Set<PerformerDTO> performerDTOS = null;

        try{
            client= ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI + PERFORMERS_URI + ALL_PERFORMERS).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.get();
            if(_response.getStatus()==Response.Status.OK.getStatusCode()){
                performerDTOS=_response.readEntity(new GenericType<Set<PerformerDTO>>(){});
            }
        }catch (ServiceException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        } finally {
            processCookieFromResponse(_response);
            client.close();
        }
        return performerDTOS;
    }

    @Override
    public UserDTO createUser(UserDTO newUser) throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+USER_URI+CREATE_USER).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.post(Entity.entity(newUser,MediaType.APPLICATION_XML));
            if (_response.getStatus()==Response.Status.CREATED.getStatusCode()){
                return _response.readEntity(UserDTO.class);
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
    }

    @Override
    public UserDTO authenticateUser(UserDTO user) throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+USER_URI+AUTHENTICATE_USER).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.post(Entity.entity(user,MediaType.APPLICATION_XML));
            if (_response.getStatus()==Response.Status.OK.getStatusCode()){
                return _response.readEntity(UserDTO.class);
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }

    }

    @Override
    public Image getImageForPerformer(PerformerDTO performer) throws ServiceException {
        AWS aws =new AWS();
        try {
            String imageName=performer.getImageName();
            if(imageName==null || !getPerformers().contains(performer)){
                throw new ServiceException(Messages.NO_IMAGE_FOR_PERFORMER);
            }
            Image image=aws.fetchImage(imageName);
            if(image==null){
                throw new ServiceException(Messages.NO_IMAGE_FOR_PERFORMER);
            }
            return image;
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }


    }

    @Override
    public ReservationDTO reserveSeats(ReservationRequestDTO reservationRequest) throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+RESERVATION_URI+RESERVE_SEAT).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.post(Entity.entity(reservationRequest,MediaType.APPLICATION_XML));
            if (_response.getStatus()==Response.Status.CREATED.getStatusCode()){
                return _response.readEntity(ReservationDTO.class);
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
    }

    @Override
    public void confirmReservation(ReservationDTO reservation) throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+RESERVATION_URI+CONFIRM).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.post(Entity.entity(reservation,MediaType.APPLICATION_XML));
            if (_response.getStatus()==Response.Status.OK.getStatusCode()){
                return;
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
    }

    @Override
    public void registerCreditCard(CreditCardDTO creditCard) throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+CREDITCARD_URI+REGISTER_CREDITCARD).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.post(Entity.entity(creditCard,MediaType.APPLICATION_XML));
            if (_response.getStatus()==Response.Status.OK.getStatusCode()){
                return;
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
    }

    @Override
    public Set<BookingDTO> getBookings() throws ServiceException {
        try {
            client=ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI+RESERVATION_URI+BOOKING).request().accept(MediaType.APPLICATION_XML);
            addCookieToInvocation(builder);
            _response=builder.get();
            if (_response.getStatus()==Response.Status.OK.getStatusCode()){
                return _response.readEntity(new GenericType<Set<BookingDTO>>(){});
            } else {
                throw new ServiceException(_response.readEntity(String.class));
            }
        } catch (ProcessingException e){
            throw new ServiceException(Messages.SERVICE_COMMUNICATION_ERROR);
        }finally {
            processCookieFromResponse(_response);
            client.close();
        }
    }


    // Method to add any cookie previously returned from the Web service to an
    // Invocation.Builder instance.
    private void addCookieToInvocation(Builder builder) {
        if(!_cookieValues.isEmpty()) {
            builder.cookie(Config.COOKIE, _cookieValues.iterator().next());
        }
    }

    // Method to extract any cookie from a Response object received from the
    // Web service. If there is a cookie named clientId (Config.CLIENT_COOKIE)
    // it is added to the _cookieValues set, which stores all cookie values for
    // clientId received by the Web service.
    private void processCookieFromResponse(Response response) {
        Map<String, NewCookie> cookies = response.getCookies();

        if(cookies.containsKey(Config.COOKIE)) {
            String cookieValue = cookies.get(Config.COOKIE).getValue();
            _cookieValues.add(cookieValue);
        }
    }
}
