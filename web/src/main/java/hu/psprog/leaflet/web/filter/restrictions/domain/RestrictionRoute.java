package hu.psprog.leaflet.web.filter.restrictions.domain;

import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * Route with method wrapper for configuring security restriction validation strategies.
 *
 * @author Peter Smith
 */
@Data
public class RestrictionRoute {

    private HttpMethod method;
    private String path;
}
