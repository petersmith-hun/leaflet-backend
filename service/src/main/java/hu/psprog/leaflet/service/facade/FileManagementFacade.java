package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.vo.UploadedFileVO;

import java.util.List;

/**
 * Facade for file management operations, including info storage.
 *
 * @author Peter Smith
 */
public interface FileManagementFacade extends FileManagementService {

    /**
     * Retrieves a list of stored files.
     *
     * @return List of {@link UploadedFileVO} objects holding information about stored files
     */
    List<UploadedFileVO> retrieveStoredFileList();
}
