package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.UploadedFile;

import java.util.Optional;
import java.util.UUID;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.UploadedFileRepository}.
 *
 * @author Peter Smith
 */
public interface UploadedFileDAO {

    Optional<UploadedFile> findByPathUUID(UUID pathUUID);
}
