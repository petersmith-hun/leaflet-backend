package hu.psprog.leaflet.web.filter;

import hu.psprog.leaflet.web.filter.restrictions.domain.ClientAcceptorConfiguration;
import hu.psprog.leaflet.web.filter.restrictions.domain.RestrictionType;
import hu.psprog.leaflet.web.filter.restrictions.strategy.RestrictionValidatorStrategy;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ReflectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hu.psprog.leaflet.web.filter.ClientAcceptorFilter.HEADER_CLIENT_ID;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link ClientAcceptorFilter}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ClientAcceptorFilterTest {

    private static final Map<UUID, ClientAcceptorConfiguration> CLIENTS = new HashMap<>();
    private static final UUID CLIENT_ID_1 = UUID.randomUUID();
    private static final UUID CLIENT_ID_2 = UUID.randomUUID();
    private static final ClientAcceptorConfiguration CLIENT_ACCEPTOR_CONFIGURATION_1 = new ClientAcceptorConfiguration();
    private static final ClientAcceptorConfiguration CLIENT_ACCEPTOR_CONFIGURATION_2 = new ClientAcceptorConfiguration();
    private static final String SECURITY_CHECKS_ENABLED_PROPERTY = "leaflet-link.security-checks-enabled";
    private static final String EXCLUDED_ROUTE = "/excluded-route";

    static {
        CLIENT_ACCEPTOR_CONFIGURATION_1.setName("client1");
        CLIENT_ACCEPTOR_CONFIGURATION_1.setRestrictions(Arrays.asList(RestrictionType.DEVICE_ID, RestrictionType.CAPTCHA_TOKEN));

        CLIENT_ACCEPTOR_CONFIGURATION_2.setName("client2");
        CLIENT_ACCEPTOR_CONFIGURATION_2.setRestrictions(Collections.singletonList(RestrictionType.DEVICE_ID));

        CLIENTS.put(CLIENT_ID_1, CLIENT_ACCEPTOR_CONFIGURATION_1);
        CLIENTS.put(CLIENT_ID_2, CLIENT_ACCEPTOR_CONFIGURATION_2);
    }

    @Mock
    private RestrictionValidatorStrategy restrictionValidatorStrategy1;

    @Mock
    private RestrictionValidatorStrategy restrictionValidatorStrategy2;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private ClientAcceptorFilter clientAcceptorFilter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(restrictionValidatorStrategy1.forRestrictionType()).willReturn(RestrictionType.DEVICE_ID);
        given(restrictionValidatorStrategy2.forRestrictionType()).willReturn(RestrictionType.CAPTCHA_TOKEN);
        clientAcceptorFilter = new ClientAcceptorFilter(Arrays.asList(restrictionValidatorStrategy1, restrictionValidatorStrategy2));
        clientAcceptorFilter.setClients(CLIENTS);
    }

    @Test
    public void shouldInitFilter() {

        // when
        clientAcceptorFilter.init();

        // then
        Map<UUID, List<RestrictionValidatorStrategy>> currentMapping = extractMapping();
        assertThat(currentMapping.size(), equalTo(2));
        assertThat(currentMapping.get(CLIENT_ID_1).size(), equalTo(2));
        assertThat(currentMapping.get(CLIENT_ID_1).containsAll(Arrays.asList(restrictionValidatorStrategy1, restrictionValidatorStrategy2)), is(true));
        assertThat(currentMapping.get(CLIENT_ID_2).size(), equalTo(1));
        assertThat(currentMapping.get(CLIENT_ID_2).contains(restrictionValidatorStrategy1), is(true));
        assertThat(clientAcceptorFilter.getExcludedRoutes().isEmpty(), is(true));
        assertThat(clientAcceptorFilter.getClients(), equalTo(CLIENTS));
    }

    @Test
    public void shouldAcceptClient() throws ServletException, IOException {

        // given
        clientAcceptorFilter.init();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn(CLIENT_ID_1.toString());

        // when
        clientAcceptorFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(restrictionValidatorStrategy1).validate(request);
        verify(restrictionValidatorStrategy2).validate(request);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void shouldIgnoreRequest() throws ServletException, IOException {

        // given
        clientAcceptorFilter.setExcludedRoutes(Collections.singletonList(EXCLUDED_ROUTE));
        clientAcceptorFilter.init();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn(CLIENT_ID_1.toString());
        given(request.getRequestURI()).willReturn(EXCLUDED_ROUTE);

        // when
        clientAcceptorFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(restrictionValidatorStrategy1, never()).validate(request);
        verify(restrictionValidatorStrategy2, never()).validate(request);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void shouldWrapMissingClientIDExceptionOnEmptyClientID() throws ServletException, IOException {

        // given
        clientAcceptorFilter.init();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn(null);

        // when
        callFilterForException();
    }

    @Test
    public void shouldWrapMissingClientIDExceptionOnMalformedClientID() throws ServletException, IOException {

        // given
        clientAcceptorFilter.init();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn("malformed-uuid");

        // when
        callFilterForException();
    }

    @Test
    public void shouldWrapUnknownClientException() throws ServletException, IOException {

        // given
        clientAcceptorFilter.init();
        given(request.getHeader(HEADER_CLIENT_ID)).willReturn(UUID.randomUUID().toString());

        // when
        callFilterForException();
    }

    private void callFilterForException() throws ServletException, IOException {

        // when
        clientAcceptorFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verifyZeroInteractions(filterChain);
        verify(restrictionValidatorStrategy1, never()).validate(request);
        verify(restrictionValidatorStrategy2, never()).validate(request);
    }

    private Map<UUID, List<RestrictionValidatorStrategy>> extractMapping() {

        Field mappingField = ReflectionUtils.findField(ClientAcceptorFilter.class, "clientValidationMapping");
        mappingField.setAccessible(true);

        Map<UUID, List<RestrictionValidatorStrategy>> mapping = null;
        try {
            mapping = (Map<UUID, List<RestrictionValidatorStrategy>>) mappingField.get(clientAcceptorFilter);
        } catch (IllegalAccessException e) {
            fail("Could not extract mapping");
        }

        return mapping;
    }
}