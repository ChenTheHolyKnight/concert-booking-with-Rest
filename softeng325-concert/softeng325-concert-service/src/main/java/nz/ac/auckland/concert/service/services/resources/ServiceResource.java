package nz.ac.auckland.concert.service.services.resources;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.util.UUID;

import static nz.ac.auckland.concert.common.Config.COOKIE;

public abstract class ServiceResource {
    protected static final int EXPIRY_TIME = 5;


    protected static NewCookie makeCookie(Cookie clientId) {
        NewCookie newCookie;
        if (clientId == null) {
            newCookie = new NewCookie(COOKIE, UUID.randomUUID().toString());
        } else {
            newCookie = new NewCookie(clientId);
        }
        return newCookie;
    }


}
