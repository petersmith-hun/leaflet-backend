package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * DAO implementation of {@link UploadedFileRepository}.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileDAOImpl extends SelfStatusAwareDAOImpl<UploadedFile, Long> implements UploadedFileDAO {

    @Autowired
    public UploadedFileDAOImpl(final UploadedFileRepository uploadedFileRepository) {
        super(uploadedFileRepository);
    }

    @Override
    public UploadedFile findByPath(String path) {
        return ((UploadedFileRepository) jpaRepository).findByPath(path);
    }

    @Transactional
    @Override
    public UploadedFile updateOne(Long id, UploadedFile updatedEntity) {

        UploadedFile currentUploadedFile = jpaRepository.getOne(id);
        if (Objects.nonNull(currentUploadedFile)) {
            currentUploadedFile.setOriginalFilename(updatedEntity.getOriginalFilename());
            currentUploadedFile.setLastModified(new Date());
            jpaRepository.flush();
        }

        return currentUploadedFile;
    }
}
