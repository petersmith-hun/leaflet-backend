package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.io.OutputStream;

/**
 * Service to handle file operations.
 * Supports uploading new files and retrieve existing ones.
 * Also handles storage status information management in database.
 *
 * @author Peter Smith
 */
public interface FileManagementService {

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
     *
     * @param filename filename of the file to download
     * @return uploaded file as {@link OutputStream}
     * @throws ServiceException if there's no existing file by the given filename
     */
    OutputStream download(String filename) throws ServiceException;

    /**
     * Removes an existing file by its stored filename.
     *
     * @param filename filename of the file to remove
     * @throws ServiceException if there's no existing file by the given filename
     */
    void remove(String filename) throws ServiceException;

    /**
     * Creates a new directory under given parent directory.
     *
     * @param parent parent directory name (must be already existing)
     * @param directoryName name of the directory to create
     */
    void createDirectory(String parent, String directoryName);
}
