package hu.psprog.leaflet.service.config;

import hu.psprog.leaflet.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

/**
 * Additional service layer configuration.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(ServiceConfiguration.PERSISTENCE_FACADE_PACKAGE)
public class ServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

    public static final String PERSISTENCE_FACADE_PACKAGE = "hu.psprog.leaflet.persistence.dao";

    @Value("${uploads.directory}")
    @NotNull
    private String storageRootDirectoryPath;

    @Bean
    public File fileStorage() throws ServiceException {
        try {
            File storageRootDirectory = new File(storageRootDirectoryPath);
            if (!storageRootDirectory.exists()) {
                createStorageRoot(storageRootDirectory);
            }
            if (!checkStorageRootAccessibility(storageRootDirectory)) {
                throw new ServiceException("Storage root is not accessible.");
            }
            LOGGER.info("File storage attached at [{}].", storageRootDirectoryPath);

            return storageRootDirectory;
        } catch (IOException exception) {
            LOGGER.error("Failed to initialize file storage. Stopping context.");
            throw new ServiceException("Failed to initialize file storage because of the following exception: ", exception);
        }
    }

    private boolean checkStorageRootAccessibility(File storageRootDirectory) {
        return storageRootDirectory.canRead() && storageRootDirectory.canWrite();
    }

    private void createStorageRoot(File storageRootDirectory) throws IOException {
        LOGGER.info("File storage root does not exist. Trying to create...");
        if (storageRootDirectory.mkdirs()) {
            LOGGER.info("Storage root created at [{}].", storageRootDirectoryPath);
        } else {
            LOGGER.error("Failed to create storage root at [{}]", storageRootDirectoryPath);
        }
    }
}
