package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;
import java.util.UUID;

/**
 * Facade for file management operations, including info storage.
 *
 * @author Peter Smith
 */
public interface FileManagementFacade {

    /**
     * Handles file upload.
     *
     * @param fileInputVO source file information (which is being uploaded)
     * @return uploaded file information
     * @throws ServiceException on file upload failure
     */
    UploadedFileVO upload(FileInputVO fileInputVO) throws ServiceException;

    /**
     * Downloads an existing file by its stored filename.
     * Path should be relative to the storage root.
     *
     * @param pathUUID pathUUID of the file to download
     * @return uploaded file and its meta information as {@link DownloadableFileWrapperVO}
     * @throws ServiceException if there's no existing file by the given filename
     */
    DownloadableFileWrapperVO download(UUID pathUUID) throws ServiceException;

    /**
     * Removes an existing file by its stored filename.
     *
     * @param pathUUID filename of the file to remove
     * @throws ServiceException if there's no existing file by the given filename
     */
    void remove(UUID pathUUID) throws ServiceException;

    /**
     * Creates a new directory under given parent directory.
     *
     * @param parent parent directory name (must be already existing)
     * @param directoryName name of the directory to create
     */
    void createDirectory(String parent, String directoryName) throws ServiceException;

    /**
     * Retrieves a list of stored files.
     *
     * @return List of {@link UploadedFileVO} objects holding information about stored files
     */
    List<UploadedFileVO> retrieveStoredFileList();
}
