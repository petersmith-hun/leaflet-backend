package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;

/**
 * Uploaded file meta info operations interface.
 *
 * @author Peter Smith
 */
public interface FileMetaInfoService {

    /**
     * Retrieves meta information for given path.
     *
     * @param path path of the file to retrieve meta information of
     * @return meta information as {@link UploadedFileVO}
     */
    UploadedFileVO retrieveMetaInfo(String path) throws ServiceException;

    /**
     * Stores meta information of uploaded file.
     *
     * @param uploadedFileVO uploaded file information as {@link UploadedFileVO}.
     * @return ID of created meta info object
     */
    Long storeMetaInfo(UploadedFileVO uploadedFileVO) throws ServiceException;

    /**
     * Removes meta information for given file by path.
     *
     * @param path path of the file to remove meta information of
     */
    void removeMetaInfo(String path) throws ServiceException;

    /**
     * Changes original filename.
     *
     * @param path path of existing file to rename
     * @param newFilename new filename as string
     */
    void rename(String path, String newFilename) throws ServiceException;

    /**
     * Retrieves meta information for all uploaded files.
     *
     * @return List of {@link UploadedFileVO} objects
     */
    List<UploadedFileVO> getUploadedFiles();
}
