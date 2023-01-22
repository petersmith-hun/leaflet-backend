package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Facade for file management operations, including info storage.
 *
 * @author Peter Smith
 * @deprecated LSRS service is taking over the respective functionality
 */
@Deprecated(forRemoval = true)
public interface FileManagementFacade {

    /**
     * Handles file upload.
     *
     * @param fileInputVO source file information (which is being uploaded)
     * @return uploaded file information
     * @throws ServiceException on file upload failure
     */
    @Deprecated(forRemoval = true)
    UploadedFileVO upload(FileInputVO fileInputVO) throws ServiceException;

    /**
     * Downloads an existing file by its stored filename.
     * Path should be relative to the storage root.
     *
     * @param pathUUID pathUUID of the file to download
     * @return uploaded file and its meta information as {@link DownloadableFileWrapperVO}
     * @throws ServiceException if there's no existing file by the given filename
     */
    @Deprecated(forRemoval = true)
    DownloadableFileWrapperVO download(UUID pathUUID) throws ServiceException;

    /**
     * Removes an existing file by its stored filename.
     *
     * @param pathUUID pathUUID of the file to remove
     * @throws ServiceException if there's no existing file by the given filename
     */
    @Deprecated(forRemoval = true)
    void remove(UUID pathUUID) throws ServiceException;

    /**
     * Creates a new directory under given parent directory.
     *
     * @param parent parent directory name (must be already existing)
     * @param directoryName name of the directory to create
     */
    @Deprecated(forRemoval = true)
    void createDirectory(String parent, String directoryName) throws ServiceException;

    /**
     * Retrieves a list of stored files.
     *
     * @return List of {@link UploadedFileVO} objects holding information about stored files
     */
    @Deprecated(forRemoval = true)
    List<UploadedFileVO> retrieveStoredFileList();

    /**
     * Updates file meta information.
     *
     * @param pathUUID pathUUID of existing file to update meta info of
     * @param updateFileMetaInfoVO updated meta information
     * @throws ServiceException if there's no existing file by the given filename
     */
    @Deprecated(forRemoval = true)
    void updateMetaInfo(UUID pathUUID, UpdateFileMetaInfoVO updateFileMetaInfoVO) throws ServiceException;

    /**
     * Retrieves given file's meta info only if the file exists.
     *
     * @param pathUUID pathUUID of the file to get meta info of
     * @return UploadedFileVO wrapped in Optional if the file exists, empty Optional otherwise
     */
    @Deprecated(forRemoval = true)
    Optional<UploadedFileVO> getCheckedMetaInfo(UUID pathUUID);

    /**
     * Retrieves information of existing acceptors.
     *
     * @return List of {@link AcceptorInfoVO} holding information about acceptors
     */
    @Deprecated(forRemoval = true)
    List<AcceptorInfoVO> getAcceptorInfo();
}
