package nz.ac.auckland.concert.service.services.resources;

import nz.ac.auckland.concert.common.dto.CreditCardDTO;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.xml.ws.Response;

import static nz.ac.auckland.concert.common.Config.CREDITCARD_URI;
import static nz.ac.auckland.concert.common.Config.REGISTER_CREDITCARD;

@Path(CREDITCARD_URI)
public class CreditResource extends ServiceResource{

    @POST
    @Path(REGISTER_CREDITCARD)
    public Response registerCreditCard(CreditCardDTO creditCardDTO){
        return null;
    }
}
