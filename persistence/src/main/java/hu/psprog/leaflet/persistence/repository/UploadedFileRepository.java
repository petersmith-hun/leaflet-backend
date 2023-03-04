package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UploadedFile repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    /**
     * Retrieves {@link UploadedFile} by path UUID.
     *
     * @param pathUUID path UUID value
     * @return stored file meta info as {@link UploadedFile}
     */
    Optional<UploadedFile> findByPathUUID(UUID pathUUID);
}
