package hu.psprog.leaflet.web.filter.restrictions.domain;

import lombok.Data;

import java.util.List;

/**
 * Security configuration model class for client acceptor filter.
 *
 * @author Peter Smith
 */
@Data
public class ClientAcceptorConfiguration {

    private String name;
    private List<RestrictionType> restrictions;
}
