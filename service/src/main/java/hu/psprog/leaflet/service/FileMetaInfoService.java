package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;
import java.util.UUID;

/**
 * Uploaded file meta info operations interface.
 *
 * @author Peter Smith
 */
public interface FileMetaInfoService {

    /**
     * Retrieves meta information for given pathUUID.
     *
     * @param pathUUID pathUUID of the file to retrieve meta information of
     * @return meta information as {@link UploadedFileVO}
     */
    UploadedFileVO retrieveMetaInfo(UUID pathUUID) throws ServiceException;

    /**
     * Stores meta information of uploaded file.
     *
     * @param uploadedFileVO uploaded file information as {@link UploadedFileVO}.
     * @return ID of created meta info object
     */
    Long storeMetaInfo(UploadedFileVO uploadedFileVO) throws ServiceException;

    /**
     * Removes meta information for given file by pathUUID.
     *
     * @param pathUUID pathUUID of the file to remove meta information of
     */
    void removeMetaInfo(UUID pathUUID) throws ServiceException;

    /**
     * Updates file meta information.
     *
     * @param pathUUID pathUUID of existing file to update meta info of
     * @param updateFileMetaInfoVO updated meta information
     */
    void updateMetaInfo(UUID pathUUID, UpdateFileMetaInfoVO updateFileMetaInfoVO) throws ServiceException;

    /**
     * Retrieves meta information for all uploaded files.
     *
     * @return List of {@link UploadedFileVO} objects
     */
    List<UploadedFileVO> getUploadedFiles();
}
