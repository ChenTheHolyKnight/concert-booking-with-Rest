package nz.ac.auckland.concert.client.service;

import nz.ac.auckland.concert.common.dto.*;
import nz.ac.auckland.concert.service.domain.model.Performer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.Set;

public class DefaultService implements ConcertService{

    Client client;
    private final static String WEB_SERVICE_URI="http://localhost:10000/services";
    private final static String CONCERTS_URI="/concert/concerts";
    private final static String PERFORMERS_URI="/performer/performers";





    @Override
    public Set<ConcertDTO> getConcerts() throws ServiceException {
        Set<ConcertDTO> concertDTOS = null;

        try{
            client= ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI + CONCERTS_URI).request().accept(MediaType.APPLICATION_XML);
            Response response=builder.get();
            System.out.println("Rob"+response.getStatus());
            if(response.getStatus()==Response.Status.OK.getStatusCode()){
                concertDTOS=response.readEntity(new GenericType<Set<ConcertDTO>>(){});
            }
        }catch (Exception e){
            System.out.println("not working");
        }
        return concertDTOS;
    }

    @Override
    public Set<PerformerDTO> getPerformers() throws ServiceException {
        Set<PerformerDTO> performerDTOS = null;

        try{
            client= ClientBuilder.newClient();
            Builder builder=client.target(WEB_SERVICE_URI + PERFORMERS_URI).request().accept(MediaType.APPLICATION_XML);
            Response response=builder.get();
            System.out.println("Rob"+response.getStatus());
            if(response.getStatus()==Response.Status.OK.getStatusCode()){
                performerDTOS=response.readEntity(new GenericType<Set<PerformerDTO>>(){});
            }
        }catch (Exception e){
            System.out.println("not working");
        }
        return performerDTOS;
    }

    @Override
    public UserDTO createUser(UserDTO newUser) throws ServiceException {
        return null;
    }

    @Override
    public UserDTO authenticateUser(UserDTO user) throws ServiceException {
        return null;
    }

    @Override
    public Image getImageForPerformer(PerformerDTO performer) throws ServiceException {
        return null;
    }

    @Override
    public ReservationDTO reserveSeats(ReservationRequestDTO reservationRequest) throws ServiceException {
        return null;
    }

    @Override
    public void confirmReservation(ReservationDTO reservation) throws ServiceException {

    }

    @Override
    public void registerCreditCard(CreditCardDTO creditCard) throws ServiceException {

    }

    @Override
    public Set<BookingDTO> getBookings() throws ServiceException {
        return null;
    }
}
