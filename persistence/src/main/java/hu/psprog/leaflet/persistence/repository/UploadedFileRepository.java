package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.springframework.stereotype.Repository;

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
}
