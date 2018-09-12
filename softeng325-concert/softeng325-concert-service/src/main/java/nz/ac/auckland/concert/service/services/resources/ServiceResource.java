package nz.ac.auckland.concert.service.services.resources;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.util.UUID;

public abstract class ServiceResource {
    protected static final String COOKIE = "clientId";

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
