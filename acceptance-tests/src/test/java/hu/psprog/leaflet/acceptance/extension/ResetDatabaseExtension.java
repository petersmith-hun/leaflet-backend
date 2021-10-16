package hu.psprog.leaflet.acceptance.extension;

import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Database reset extension for Leaflet acceptance test suites.
 *
 * @author Peter Smith
 */
public class ResetDatabaseExtension extends AbstractTransactionalJUnit4SpringContextTests implements Extension, AfterEachCallback, BeforeAllCallback {

    private static final String DATABASE_INIT_SCRIPT_LOCATION = "classpath:data.sql";
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
            "leaflet_users",
            "leaflet_front_end_routes"
    };

    @Override
    public void beforeAll(ExtensionContext context) {
        setupApplicationContext(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        resetDatabase(context);
    }

    private void setupApplicationContext(ExtensionContext context) {

        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        setApplicationContext(applicationContext);
        setDataSource(applicationContext.getBean(DataSource.class));
    }

    private void resetDatabase(ExtensionContext context) {

        context.getTestMethod().ifPresent(method -> {
            if (Objects.nonNull(method.getAnnotation(ResetDatabase.class))) {
                deleteFromTables(DATABASE_TABLES);
                executeSqlScript(DATABASE_INIT_SCRIPT_LOCATION, false);
            }
        });
    }
}
