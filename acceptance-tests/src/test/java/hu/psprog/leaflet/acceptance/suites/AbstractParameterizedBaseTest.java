package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.mockito.BDDMockito.given;

/**
 * Base test class defining JUnit rules.
 *
 * @author Peter Smith
 */
public abstract class AbstractParameterizedBaseTest {

    private static final String CONTROL_MODELS = "control_models/control-%s%s.json";

    static final String CONTROL_MENU = "menu";
    static final String CONTROL_SUFFIX_EDIT = "edit";
    static final String CONTROL_SUFFIX_MODIFY = "modify";
    static final String CONTROL_SUFFIX_CREATE = "create";

    static final String RECAPTCHA_TOKEN = "recaptcha-token";

    @Autowired
    private JsonMapper jsonMapper;

    private Map<String, String> requestAuthenticationHeaderTemp;

    @Autowired
    MockNotificationService notificationService;

    // keep it here, as context will restart if this is autowired separately in each tests
    @Autowired
    @MockitoSpyBean
    RequestAuthentication requestAuthentication;

    @AfterEach
    public void tearDown() {
        notificationService.reset();
    }

    <T> T getControl(String id, TypeReference<T> asType) {
        return getControl(id, null, asType);
    }

    <T> T getControl(String id, Class<T> asType) {
        return getControl(id, null, asType);
    }

    <T> T getControl(String id, String suffix, TypeReference<T> asType) {
        try {
            return jsonMapper.readValue(parseControlURL(id, suffix).openStream(), asType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read control object", e);
        }
    }

    <T> T getControl(String id, String suffix, Class<T> asType) {
        return getControl(id, suffix, new TypeReference<>(){
            @Override
            public Type getType() {
                return asType;
            }
        });
    }

    void clearAuthentication() {
        requestAuthenticationHeaderTemp = requestAuthentication.getAuthenticationHeader();
        given(requestAuthentication.getAuthenticationHeader()).willReturn(new HashMap<>());
    }

    void restoreAuthentication() {
        given(requestAuthentication.getAuthenticationHeader()).willReturn(requestAuthenticationHeaderTemp);
    }

    private URL parseControlURL(String id, String suffix) {
        try {
            return new ClassPathResource(String.format(CONTROL_MODELS, id, prepareSuffix(suffix))).getURL();
        } catch (IOException e) {
            throw new IllegalArgumentException("Given control object not found.", e);
        }
    }

    private String prepareSuffix(String suffix) {
        return Objects.nonNull(suffix)
                ? "-" + suffix
                : StringUtils.EMPTY;
    }
}
