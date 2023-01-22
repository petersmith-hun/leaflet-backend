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
 * @deprecated LSRS service is taking over the respective functionality
 */
@Deprecated(forRemoval = true)
public interface FileMetaInfoService {

    /**
     * Retrieves meta information for given pathUUID.
     *
     * @param pathUUID pathUUID of the file to retrieve meta information of
     * @return meta information as {@link UploadedFileVO}
     */
    @Deprecated(forRemoval = true)
    UploadedFileVO retrieveMetaInfo(UUID pathUUID) throws ServiceException;

    /**
     * Stores meta information of uploaded file.
     *
     * @param uploadedFileVO uploaded file information as {@link UploadedFileVO}.
     * @return ID of created meta info object
     */
    @Deprecated(forRemoval = true)
    Long storeMetaInfo(UploadedFileVO uploadedFileVO) throws ServiceException;

    /**
     * Removes meta information for given file by pathUUID.
     *
     * @param pathUUID pathUUID of the file to remove meta information of
     */
    @Deprecated(forRemoval = true)
    void removeMetaInfo(UUID pathUUID) throws ServiceException;

    /**
     * Updates file meta information.
     *
     * @param pathUUID pathUUID of existing file to update meta info of
     * @param updateFileMetaInfoVO updated meta information
     */
    @Deprecated(forRemoval = true)
    void updateMetaInfo(UUID pathUUID, UpdateFileMetaInfoVO updateFileMetaInfoVO) throws ServiceException;

    /**
     * Retrieves meta information for all uploaded files.
     *
     * @return List of {@link UploadedFileVO} objects
     */
    @Deprecated(forRemoval = true)
    List<UploadedFileVO> getUploadedFiles();
}
