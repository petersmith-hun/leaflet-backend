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
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FileMetaInfoService}.
 *
 * @author Peter Smith
 */
@Service
public class FileMetaInfoServiceImpl implements FileMetaInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMetaInfoServiceImpl.class);

    private static final String ENTITY_COULD_NOT_BE_PERSISTED = "Entity could not be persisted.";
    private static final String A_FILE_WITH_GIVEN_FILENAME_ALREADY_EXISTS = "A file with given filename already exists";

    private UploadedFileDAO uploadedFileDAO;
    private UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter;
    private UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter;

    @Autowired
    public FileMetaInfoServiceImpl(UploadedFileDAO uploadedFileDAO, UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter,
                                   UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter) {
        this.uploadedFileDAO = uploadedFileDAO;
        this.uploadedFileToUploadedFileVOConverter = uploadedFileToUploadedFileVOConverter;
        this.uploadedFileVOToUploadedFileConverter = uploadedFileVOToUploadedFileConverter;
    }

    @Override
    public UploadedFileVO retrieveMetaInfo(UUID pathUUID) throws ServiceException {

        UploadedFile uploadedFile = uploadedFileDAO.findByPathUUID(pathUUID);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, pathUUID);
        }

        return uploadedFileToUploadedFileVOConverter.convert(uploadedFile);
    }

    @Override
    @PermitEditorOrAdmin
    public Long storeMetaInfo(UploadedFileVO uploadedFileVO) throws ServiceException {

        UploadedFile uploadedFileToStore = uploadedFileVOToUploadedFileConverter.convert(uploadedFileVO);
        UploadedFile storedUploadedFile;
        try {
            storedUploadedFile = uploadedFileDAO.save(uploadedFileToStore);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_FILE_WITH_GIVEN_FILENAME_ALREADY_EXISTS, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (Objects.isNull(storedUploadedFile)) {
            throw new EntityCreationException(UploadedFile.class);
        }

        LOGGER.info("File meta info has been stored for [{}]", uploadedFileVO.getStoredFilename());

        return storedUploadedFile.getId();
    }

    @Override
    @PermitEditorOrAdmin
    public void removeMetaInfo(UUID pathUUID) throws ServiceException  {

        UploadedFile uploadedFile = uploadedFileDAO.findByPathUUID(pathUUID);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, pathUUID);
        }

        uploadedFileDAO.delete(uploadedFile.getId());
        LOGGER.info("File meta info has been deleted for file [{}]", uploadedFile.getStoredFilename());
    }

    @Override
    @PermitEditorOrAdmin
    public void updateMetaInfo(UUID pathUUID, UpdateFileMetaInfoVO updateFileMetaInfoVO) throws ServiceException  {

        UploadedFile uploadedFile = uploadedFileDAO.findByPathUUID(pathUUID);
        if (Objects.isNull(uploadedFile)) {
            throw new EntityNotFoundException(UploadedFile.class, pathUUID);
        }

        uploadedFile.setOriginalFilename(updateFileMetaInfoVO.getOriginalFilename());
        uploadedFile.setDescription(updateFileMetaInfoVO.getDescription());
        uploadedFileDAO.updateOne(uploadedFile.getId(), uploadedFile);
        LOGGER.info("File meta info has been updated for file [{}]", uploadedFile.getStoredFilename());
    }

    @Override
    @PermitEditorOrAdmin
    public List<UploadedFileVO> getUploadedFiles() {
        return uploadedFileDAO.findAll().stream()
                .map(uploadedFileToUploadedFileVOConverter::convert)
                .collect(Collectors.toList());
    }
}
