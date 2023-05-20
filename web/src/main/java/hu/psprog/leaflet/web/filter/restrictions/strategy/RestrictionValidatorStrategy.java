package hu.psprog.leaflet.web.filter.restrictions.strategy;

import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.ClientSecurityViolationException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Client acceptor filter validation strategy interface.
 * Implementations are able to validate if a client's request conforms a specific restriction type.
 * Implementations must specify for which restriction type they are providing validation logic,
 * and currently only one implementation for each restriction type can exist at a time.
 *
 * @author Peter Smith
 */
public interface RestrictionValidatorStrategy {

    /**
     * Validates given request for a restriction type.
     *
     * @param request {@link HttpServletRequest} to be checked
     * @throws ClientSecurityViolationException should be thrown when the client violates a restriction
     */
    void validate(HttpServletRequest request) throws ClientSecurityViolationException;

    /**
     * Specifies the restriction type for which the implementation provides validation logic.
     *
     * @return validated restriction type as {@link RestrictionType}
     */
    RestrictionType forRestrictionType();
}
