package hu.psprog.leaflet.acceptance.extension;

import hu.psprog.leaflet.acceptance.config.ClearStorage;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Clear temporal file storage extension for Leaflet acceptance tests.
 *
 * @author Peter Smith
 */
public class ClearStorageExtension implements Extension, AfterEachCallback, BeforeAllCallback {

    private String baseDirectory;

    @Override
    public void beforeAll(ExtensionContext context) {
        setupBaseDirectoryPath(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        clearStorage(context);
    }

    private void setupBaseDirectoryPath(ExtensionContext context) {

        baseDirectory = SpringExtension.getApplicationContext(context)
                .getEnvironment()
                .getProperty("java.io.tmpdir");
    }

    private void clearStorage(ExtensionContext context) {

        context.getTestMethod().ifPresent(method -> {
            ClearStorage clearStorage = method.getAnnotation(ClearStorage.class);
            if (Objects.nonNull(clearStorage)) {
                File fileToDelete = new File(baseDirectory, clearStorage.path());
                if (fileToDelete.exists() && !fileToDelete.delete()) {
                    fail("File [" + clearStorage.path() + "] created by test could not be removed.");
                }
            }
        });
    }
}
