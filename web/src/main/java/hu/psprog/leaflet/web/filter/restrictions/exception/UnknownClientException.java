package hu.psprog.leaflet.web.filter.restrictions.exception;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_USER_AGENT;

/**
 * Exception to be thrown when a client cannot be identified by its client ID.
 *
 * @author Peter Smith
 */
public class UnknownClientException extends SecurityRestrictionViolationException {

    private static final String UNKNOWN_CLIENT_MESSAGE = "Request received from unknown client [%s | %s - %s]";

    public UnknownClientException(UUID clientID, HttpServletRequest request) {
        super(String.format(UNKNOWN_CLIENT_MESSAGE, clientID, request.getRemoteAddr(), request.getHeader(HEADER_USER_AGENT)));
    }
}
