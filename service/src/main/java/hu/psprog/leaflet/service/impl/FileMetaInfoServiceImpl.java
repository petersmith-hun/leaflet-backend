package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.FileMetaInfoService;
import hu.psprog.leaflet.service.converter.UploadedFileToUploadedFileVOConverter;
import hu.psprog.leaflet.service.converter.UploadedFileVOToUploadedFileConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FileMetaInfoService}.
 *
 * @author Peter Smith
 */
@Service
public class FileMetaInfoServiceImpl implements FileMetaInfoService {

    @Autowired
    private UploadedFileDAO uploadedFileDAO;

    @Autowired
    private UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter;

    @Autowired
    private UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter;

    @Override
    public UploadedFileVO retrieveMetaInfo(String path) throws ServiceException {

        UploadedFile uploadedFile = uploadedFileDAO.findByPath(path);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, path);
        }

        return uploadedFileToUploadedFileVOConverter.convert(uploadedFile);
    }

    @Override
    public Long storeMetaInfo(UploadedFileVO uploadedFileVO) throws ServiceException {

        UploadedFile uploadedFileToStore = uploadedFileVOToUploadedFileConverter.convert(uploadedFileVO);
        UploadedFile storedUploadedFile;
        try {
            storedUploadedFile = uploadedFileDAO.save(uploadedFileToStore);
        } catch (PersistenceException exc) {
            throw new ConstraintViolationException(exc);
        }

        if (Objects.isNull(storedUploadedFile)) {
            throw new EntityCreationException(UploadedFile.class);
        }

        return storedUploadedFile.getId();
    }

    @Override
    public void removeMetaInfo(String path) throws ServiceException  {

        UploadedFile uploadedFile = uploadedFileDAO.findByPath(path);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, path);
        }

        uploadedFileDAO.delete(uploadedFile.getId());
    }

    @Override
    public void rename(String path, String newFilename) throws ServiceException  {

        UploadedFile uploadedFile = uploadedFileDAO.findByPath(path);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, path);
        }

        uploadedFile.setOriginalFilename(newFilename);
        uploadedFileDAO.updateOne(uploadedFile.getId(), uploadedFile);
    }

    @Override
    public List<UploadedFileVO> getUploadedFiles() {
        return uploadedFileDAO.findAll().stream()
                .map(uploadedFileToUploadedFileVOConverter::convert)
                .collect(Collectors.toList());
    }
}
