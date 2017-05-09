package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.file.DirectoryCreationRequestModel;
import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.api.rest.request.file.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FileManagementFacade;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Controller for file related endpoints.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_FILES)
public class FilesController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);
    private static final String PATH_FULLY_IDENTIFIED_FILE = PATH_PART_FILE_IDENTIFIER + PATH_PART_FILENAME;
    private static final String PATH_CREATE_DIRECTORY = "/directory";

    private FileManagementFacade fileManagementFacade;

    @Autowired
    public FilesController(final FileManagementFacade fileManagementFacade) {
        this.fileManagementFacade = fileManagementFacade;
    }

    /**
     * Returns list of uploaded files.
     *
     * @return list of uploaded files as {@link FileDataModel} objects wrapped in ModelAndView
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUploadedFiles() {
        List<UploadedFileVO> uploadedFiles = fileManagementFacade.retrieveStoredFileList();
        return wrap(conversionService.convert(uploadedFiles, FileListDataModel.class));
    }

    /**
     * Downloads given file.
     *
     * @param fileIdentifier UUID of the uploaded file
     * @param storedFilename stored filename of the uploaded file (currently only the fileIdentifier is used for identification)
     * @return file as resource
     * @throws ResourceNotFoundException if no existing file is found for given fileIdentifier
     */
    @RequestMapping(method = RequestMethod.GET, value = PATH_FULLY_IDENTIFIED_FILE)
    public ResponseEntity<Resource> downloadFile(@PathVariable(PATH_VARIABLE_FILE_IDENTIFIER) UUID fileIdentifier,
                                                 @PathVariable(PATH_VARIABLE_FILENAME) String storedFilename)
            throws ResourceNotFoundException {

        try {
            DownloadableFileWrapperVO downloadableFileWrapperVO = fileManagementFacade.download(fileIdentifier);
            return ResponseEntity.ok()
                    .contentLength(downloadableFileWrapperVO.getLength())
                    .contentType(MediaType.parseMediaType(downloadableFileWrapperVO.getMimeType()))
                    .body(downloadableFileWrapperVO.getFileContent());
        } catch (ServiceException e) {
            LOGGER.error("Failed to download given file.", e);
            throw new ResourceNotFoundException("Requested file can not be downloaded, probably not existing.");
        }
    }

    /**
     * Uploads a new file.
     *
     * @param fileUploadRequestModel file to upload and additional data wrapped as {@link FileUploadRequestModel}
     * @param bindingResult validation result
     * @return data of uploaded file as {@link FileDataModel}
     * @throws RequestCouldNotBeFulfilledException if file can not be uploaded
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView uploadFile(@Valid final FileUploadRequestModel fileUploadRequestModel,
                                   BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            FileInputVO fileInputVO = conversionService.convert(fileUploadRequestModel, FileInputVO.class);
            try {
                UploadedFileVO uploadedFile = fileManagementFacade.upload(fileInputVO);
                return wrap(conversionService.convert(uploadedFile, FileDataModel.class));
            } catch (ServiceException e) {
                LOGGER.error("Failed to upload file.", e);
                throw new RequestCouldNotBeFulfilledException("Failed to upload file.");
            }
        }
    }

    /**
     * Deletes an existing file.
     *
     * @param fileIdentifier UUID of the uploaded file
     * @param storedFilename stored filename of the uploaded file (currently only the fileIdentifier is used for identification)
     * @throws ResourceNotFoundException if no existing file is found for given fileIdentifier
     */
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_FULLY_IDENTIFIED_FILE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable(PATH_VARIABLE_FILE_IDENTIFIER) UUID fileIdentifier,
                           @PathVariable(PATH_VARIABLE_FILENAME) String storedFilename)
            throws ResourceNotFoundException {

        try {
            fileManagementFacade.remove(fileIdentifier);
        } catch (ServiceException e) {
            LOGGER.error("Failed to delete given file.", e);
            throw new ResourceNotFoundException("Requested file can not be deleted, probably not existing.");
        }
    }

    /**
     * Creates a new directory under given parent directory.
     *
     * @param directoryCreationRequestModel model holding directory information
     * @param bindingResult validation result
     * @return validation result on validation error, {@code null} otherwise
     * @throws RequestCouldNotBeFulfilledException if directory cannot be created
     */
    @RequestMapping(method = RequestMethod.POST, value = PATH_CREATE_DIRECTORY)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView createDirectory(@RequestBody @Valid DirectoryCreationRequestModel directoryCreationRequestModel,
                                        BindingResult bindingResult) throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                fileManagementFacade.createDirectory(directoryCreationRequestModel.getParent(), directoryCreationRequestModel.getName());
            } catch (ServiceException e) {
                LOGGER.error("Failed to create directory.", e);
                throw new RequestCouldNotBeFulfilledException("Failed to create directory.", e);
            }
            return null;
        }
    }

    /**
     * Updates given file's meta information.
     *
     * @param fileIdentifier UUID of the uploaded file
     * @param storedFilename stored filename of the uploaded file (currently only the fileIdentifier is used for identification)
     * @param updateFileMetaInfoRequestModel updated meta information
     * @param bindingResult validation result
     * @return validation result on validation error, {@code null} otherwise
     * @throws ResourceNotFoundException if no existing file is found for given fileIdentifier
     */
    @RequestMapping(method = RequestMethod.PUT, value = PATH_FULLY_IDENTIFIED_FILE)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView updateFileMetaInfo(@PathVariable(PATH_VARIABLE_FILE_IDENTIFIER) UUID fileIdentifier,
                                           @PathVariable(PATH_VARIABLE_FILENAME) String storedFilename,
                                           @RequestBody @Valid UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel,
                                           BindingResult bindingResult) throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            UpdateFileMetaInfoVO updateFileMetaInfoVO = conversionService.convert(updateFileMetaInfoRequestModel, UpdateFileMetaInfoVO.class);
            try {
                fileManagementFacade.updateMetaInfo(fileIdentifier, updateFileMetaInfoVO);
                return null;
            } catch (ServiceException e) {
                LOGGER.error("Failed to update given file.", e);
                throw new ResourceNotFoundException("Requested file can not be updated, probably not existing.");
            }
        }
    }
}
