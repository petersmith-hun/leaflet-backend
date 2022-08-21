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

/**
 * Additional service layer configuration.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(ServiceConfiguration.PERSISTENCE_DAO_PACKAGE)
public class ServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

    static final String PERSISTENCE_DAO_PACKAGE = "hu.psprog.leaflet.persistence.dao";

    public static final String AUTH_OPERATION_DEPRECATED_SINCE = "v1.13.0";

    @Value("${files.upload.storage-path}")
    @NotNull
    private String storageRootDirectoryPath;

    @Bean
    public File fileStorage() throws ServiceException {
        File storageRootDirectory = new File(storageRootDirectoryPath);
        if (!storageRootDirectory.exists()) {
            createStorageRoot(storageRootDirectory);
        }
        if (!checkStorageRootAccessibility(storageRootDirectory)) {
            throw new ServiceException("Storage root is not accessible.");
        }
        LOGGER.info("File storage attached at [{}].", storageRootDirectoryPath);

        return storageRootDirectory;
    }

    private boolean checkStorageRootAccessibility(File storageRootDirectory) {
        return storageRootDirectory.canRead() && storageRootDirectory.canWrite();
    }

    private void createStorageRoot(File storageRootDirectory) throws ServiceException {
        LOGGER.info("File storage root does not exist. Trying to create...");
        if (storageRootDirectory.mkdirs()) {
            LOGGER.info("Storage root created at [{}].", storageRootDirectoryPath);
        } else {
            LOGGER.error("Failed to create storage root at [{}]", storageRootDirectoryPath);
            throw new ServiceException("Failed to initialize file storage. Stopping context.");
        }
    }
}
