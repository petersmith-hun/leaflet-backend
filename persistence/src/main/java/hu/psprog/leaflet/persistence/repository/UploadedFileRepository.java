package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * UploadedFile repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface UploadedFileRepository extends SelfStatusAwareJpaRepository<UploadedFile, Long> {

    /**
     * Retrieves {@link UploadedFile} by path.
     *
     * @param path path value
     * @return stored file meta info as {@link UploadedFile}
     */
    UploadedFile findByPath(String path);

    /**
     * Retrieves {@link UploadedFile} by path UUID.
     *
     * @param pathUUID path UUID value
     * @return stored file meta info as {@link UploadedFile}
     */
    UploadedFile findByPathUUID(UUID pathUUID);
}
