package hu.psprog.leaflet.acceptance.suites;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.acceptance.mock.MockNotificationService;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import javax.ws.rs.core.GenericType;
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
public abstract class AbstractParameterizedBaseTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String CONTROL_MODELS = "control_models/control-%s%s.json";
    private static final String DATABASE_INIT_SCRIPT_LOCATION = "classpath:data.sql";

    static final String CONTROL_SUFFIX_EDIT = "edit";
    static final String CONTROL_SUFFIX_MODIFY = "modify";
    static final String CONTROL_SUFFIX_CREATE = "create";

    private static final String[] DATABASE_TABLES = {
            "leaflet_dynamic_config_properties",
            "leaflet_documents",
            "leaflet_comments",
            "leaflet_entries_tags",
            "leaflet_entries_uploaded_files",
            "leaflet_entries",
            "leaflet_categories",
            "leaflet_tags",
            "leaflet_uploaded_files",
            "leaflet_users"
    };

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final ResetDatabaseRule resetDatabaseRule = new ResetDatabaseRule();

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, String> requestAuthenticationHeaderTemp;

    @Autowired
    MockNotificationService notificationService;

    // keep it here, as context will restart if this is autowired separately in each tests
    @Autowired
    @SpyBean
    RequestAuthentication requestAuthentication;

    @After
    public void tearDown() {
        notificationService.reset();
    }

    <T> T getControl(String id, GenericType<T> asType) {
        return getControl(id, null, asType);
    }

    <T> T getControl(String id, Class<T> asType) {
        return getControl(id, null, asType);
    }

    <T> T getControl(String id, String suffix, GenericType<T> asType) {
        try {
            return objectMapper.readValue(parseControlURL(id, suffix), new TypeReference<T>() {
                @Override
                public Type getType() {
                    return asType.getType();
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read control object", e);
        }
    }

    <T> T getControl(String id, String suffix, Class<T> asType) {
        return getControl(id, suffix, new GenericType<>(asType));
    }

    void clearAuthentication() {
        requestAuthenticationHeaderTemp = requestAuthentication.getAuthenticationHeader();
        given(requestAuthentication.getAuthenticationHeader()).willReturn(new HashMap<>());
    }

    void restoreAuthentication() {
        given(requestAuthentication.getAuthenticationHeader()).willReturn(requestAuthenticationHeaderTemp);
    }

    private void resetDatabase() {
        deleteFromTables(DATABASE_TABLES);
        executeSqlScript(DATABASE_INIT_SCRIPT_LOCATION, false);
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

    public class ResetDatabaseRule implements MethodRule {

        @Override
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            return new Statement() {

                @Override
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    } finally {
                        if (Objects.nonNull(method.getAnnotation(ResetDatabase.class))) {
                            resetDatabase();
                        }
                    }
                }
            };
        }
    }
}
