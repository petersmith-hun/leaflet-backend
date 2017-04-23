package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;

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
     * @param path path of the file to download
     * @return uploaded file and its meta information as {@link DownloadableFileWrapperVO}
     * @throws ServiceException if there's no existing file by the given filename
     */
    DownloadableFileWrapperVO download(String path) throws ServiceException;

    /**
     * Removes an existing file by its stored filename.
     *
     * @param path filename of the file to remove
     * @throws ServiceException if there's no existing file by the given filename
     */
    void remove(String path) throws ServiceException;

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
