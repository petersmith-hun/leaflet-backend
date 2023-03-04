package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * DAO implementation of {@link UploadedFileRepository}.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileDAOImpl implements UploadedFileDAO {

    private final UploadedFileRepository uploadedFileRepository;

    @Autowired
    public UploadedFileDAOImpl(final UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    @Override
    public Optional<UploadedFile> findByPathUUID(UUID pathUUID) {
        return uploadedFileRepository.findByPathUUID(pathUUID);
    }
}
