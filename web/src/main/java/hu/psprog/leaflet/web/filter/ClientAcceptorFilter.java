package hu.psprog.leaflet.web.filter;

import hu.psprog.leaflet.web.filter.restrictions.domain.ClientAcceptorConfiguration;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.exception.MissingClientIDHeaderException;
import hu.psprog.leaflet.web.filter.restrictions.exception.SecurityRestrictionViolationException;
import hu.psprog.leaflet.web.filter.restrictions.exception.UnknownClientException;
import hu.psprog.leaflet.web.filter.restrictions.strategy.RestrictionValidatorStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Filter to recognize Leaflet client applications and force them using required security measures.
 *
 * Enabled clients can be configured via 'leaflet-link' configuration parameter, which has two main sub-keys:
 *  - security-checks-enabled: by setting this parameter to false, filter can be entirely switched off
 *    Neither client identification, nor forcing required security measures will be done in this case.
 *  - clients: registering clients.
 *    Key is a client UUID - the same UUID must be sent by the client.
 *    Value is a configuration map, consisting of two keys:
 *     - name: actual client name for the client ID (only for easy identification)
 *     - restrictions: list of security measures - values can be the ones defined in {@link RestrictionType} enum
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "leaflet-link")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Conditional(ClientAcceptorFilter.ClientAcceptorFilterCondition.class)
public class ClientAcceptorFilter extends OncePerRequestFilter {

    public static final String HEADER_CLIENT_ID = "X-Client-ID";
    public static final String HEADER_USER_AGENT = "User-Agent";

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAcceptorFilter.class);
    private static final String ACCEPTED_CLIENT_LOG_MESSAGE = "Registering client [{}] with ID [{}], requiring security restrictions {}";

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private Map<UUID, ClientAcceptorConfiguration> clients;
    private List<String> excludedRoutes;
    private Map<RestrictionType, RestrictionValidatorStrategy> restrictionValidatorStrategies;
    private Map<UUID, List<RestrictionValidatorStrategy>> clientValidationMapping;

    @Autowired
    public ClientAcceptorFilter(List<RestrictionValidatorStrategy> restrictionValidatorStrategies) {
        this.restrictionValidatorStrategies = restrictionValidatorStrategies.stream()
                .collect(Collectors.toMap(RestrictionValidatorStrategy::forRestrictionType, Function.identity()));
    }

    @PostConstruct
    public void init() {
        clients.forEach((key, value) -> LOGGER.info(ACCEPTED_CLIENT_LOG_MESSAGE, value.getName(), key, value.getRestrictions()));
        clientValidationMapping = clients.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getRestrictions().stream()
                        .map(restrictionValidatorStrategies::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())));
        if (Objects.isNull(excludedRoutes)) {
            excludedRoutes = Collections.emptyList();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (!isRouteExcluded(request)) {
                UUID clientID = extractClientID(request);
                Optional.ofNullable(clientValidationMapping.get(clientID))
                        .orElseThrow(() -> new UnknownClientException(clientID, request))
                        .forEach(strategy -> strategy.validate(request));
            }
            filterChain.doFilter(request, response);
        } catch (SecurityRestrictionViolationException exc) {
            LOGGER.error("Error occurred while performing security validation of request", exc);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, exc.getMessage());
        }
    }

    public Map<UUID, ClientAcceptorConfiguration> getClients() {
        return clients;
    }

    public void setClients(Map<UUID, ClientAcceptorConfiguration> clients) {
        this.clients = clients;
    }

    public List<String> getExcludedRoutes() {
        return excludedRoutes;
    }

    public void setExcludedRoutes(List<String> excludedRoutes) {
        this.excludedRoutes = excludedRoutes;
    }

    private boolean isRouteExcluded(HttpServletRequest request) {
        return excludedRoutes.stream()
                .anyMatch(route -> PATH_MATCHER.match(route, request.getRequestURI()));
    }

    private UUID extractClientID(HttpServletRequest request) {

        if (StringUtils.isEmpty(request.getHeader(HEADER_CLIENT_ID))) {
            LOGGER.error("Client ID not found in request from [{} - {}]", request.getRemoteAddr(), request.getHeader(HEADER_USER_AGENT));
            throw new MissingClientIDHeaderException();
        }

        UUID clientID;
        try {
            clientID = UUID.fromString(request.getHeader(HEADER_CLIENT_ID));
        } catch (IllegalArgumentException exc) {
            LOGGER.error("Malformed client ID received: [{} | {} - {}]", request.getHeader(HEADER_CLIENT_ID), request.getRemoteAddr(), request.getHeader(HEADER_USER_AGENT), exc);
            throw new MissingClientIDHeaderException();
        }

        return clientID;
    }

    /**
     * Filter condition so it can be switched entirely off by configuration.
     */
    static class ClientAcceptorFilterCondition implements Condition {

        private static final String LEAFLET_LINK_SECURITY_CHECKS_ENABLED = "leaflet-link.security-checks-enabled";

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return context.getEnvironment().getProperty(LEAFLET_LINK_SECURITY_CHECKS_ENABLED, Boolean.class);
        }
    }
}
