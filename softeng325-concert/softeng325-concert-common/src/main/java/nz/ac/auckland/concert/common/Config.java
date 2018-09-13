package nz.ac.auckland.concert.common;

public class Config {

    public final static String WEB_SERVICE_URI="http://localhost:10000/services";

    //URI for the concert
    public final static String CONCERTS_URI="/concert";

    public final static String ALL_CONCERTS="/concerts";

    //URI for the performer
    public final static String PERFORMERS_URI="/performer";

    public final static String ALL_PERFORMERS="/performers";

    //URI for the user
    public final static String USER_URI="/user";

    public final static String CREATE_USER="/create";

    public final static String AUTHENTICATE_USER = "/authenticate";

    //URI for the credit card
    public final static String CREDITCARD_URI="/creditcard";

    public final static String REGISTER_CREDITCARD = "/register";

    //URI for the cookie
    public static final String COOKIE = "clientId";
}
