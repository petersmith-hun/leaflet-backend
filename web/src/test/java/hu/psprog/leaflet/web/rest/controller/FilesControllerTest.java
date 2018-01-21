package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.file.DirectoryCreationRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FileManagementFacade;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FilesController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FilesControllerTest extends AbstractControllerBaseTest {

    private static final List<UploadedFileVO> UPLOADED_FILE_VO_LIST = Collections.singletonList(UPLOADED_FILE_VO);
    private static final byte[] CONTENT_BYTE_ARRAY = "content".getBytes();
    private static final ByteArrayResource FILE_CONTENT = new ByteArrayResource(CONTENT_BYTE_ARRAY);
    private static final String MIME_TYPE = "image/jpeg";
    private static final String LOCATION_HEADER = "/files" + FILE_DATA_MODEL.getReference();

    @Mock
    private FileManagementFacade fileManagementFacade;

    @InjectMocks
    private FilesController controller;

    @Before
    public void setup() {
        super.setup();
        given(conversionService.convert(UPLOADED_FILE_VO_LIST, FileListDataModel.class)).willReturn(FILE_LIST_DATA_MODEL);
        given(conversionService.convert(UPLOADED_FILE_VO, FileDataModel.class)).willReturn(FILE_DATA_MODEL);
    }

    @Test
    public void shouldGetUploadedFiles() {

        // given
        given(fileManagementFacade.retrieveStoredFileList()).willReturn(UPLOADED_FILE_VO_LIST);

        // when
        ResponseEntity<FileListDataModel> result = controller.getUploadedFiles();

        // then
        assertResponse(result, HttpStatus.OK, FILE_LIST_DATA_MODEL);
    }

    @Test
    public void shouldDownloadFile() throws ServiceException, ResourceNotFoundException {

        // given
        given(fileManagementFacade.download(UPLOADED_FILE_VO.getPathUUID())).willReturn(prepareDownloadableFileWrapperVO());

        // when
        ResponseEntity<Resource> result = controller.downloadFile(UPLOADED_FILE_VO.getPathUUID(), null);

        // then
        assertThat(result.getBody(), equalTo(FILE_CONTENT));
        assertThat(result.getHeaders().getContentLength(), equalTo((long) CONTENT_BYTE_ARRAY.length));
        assertThat(result.getHeaders().getContentType(), equalTo(MediaType.IMAGE_JPEG));
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDownloadFileWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(fileManagementFacade).download(UPLOADED_FILE_VO.getPathUUID());

        // when
        controller.downloadFile(UPLOADED_FILE_VO.getPathUUID(), null);

        // then
        // exception expected
    }

    @Test
    public void shouldUploadFile() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(FILE_UPLOAD_REQUEST_MODEL, FileInputVO.class)).willReturn(FILE_INPUT_VO);
        given(fileManagementFacade.upload(FILE_INPUT_VO)).willReturn(UPLOADED_FILE_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.uploadFile(FILE_UPLOAD_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, FILE_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUploadFileWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.uploadFile(FILE_UPLOAD_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldUploadFileWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(FILE_UPLOAD_REQUEST_MODEL, FileInputVO.class)).willReturn(FILE_INPUT_VO);
        doThrow(ServiceException.class).when(fileManagementFacade).upload(FILE_INPUT_VO);

        // when
        controller.uploadFile(FILE_UPLOAD_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteFile() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteFile(UPLOADED_FILE_VO.getPathUUID());

        // then
        verify(fileManagementFacade).remove(UPLOADED_FILE_VO.getPathUUID());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteFileWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(fileManagementFacade).remove(UPLOADED_FILE_VO.getPathUUID());

        // when
        controller.deleteFile(UPLOADED_FILE_VO.getPathUUID());

        // then
        // exception expected
    }

    @Test
    public void shouldCreateDirectory() throws RequestCouldNotBeFulfilledException, ServiceException {

        // given
        DirectoryCreationRequestModel directoryCreationRequestModel = prepareDirectoryCreationRequestModel();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createDirectory(directoryCreationRequestModel, bindingResult);

        // then
        verify(fileManagementFacade).createDirectory(directoryCreationRequestModel.getParent(), directoryCreationRequestModel.getName());
        assertResponse(result, HttpStatus.CREATED, null);
    }

    @Test
    public void shouldCreateDirectoryWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();
        DirectoryCreationRequestModel directoryCreationRequestModel = prepareDirectoryCreationRequestModel();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createDirectory(directoryCreationRequestModel, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateDirectoryWithServiceException() throws RequestCouldNotBeFulfilledException, ServiceException {

        // given
        DirectoryCreationRequestModel directoryCreationRequestModel = prepareDirectoryCreationRequestModel();
        doThrow(ServiceException.class).when(fileManagementFacade).createDirectory(directoryCreationRequestModel.getParent(), directoryCreationRequestModel.getName());

        // when
        controller.createDirectory(directoryCreationRequestModel, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateFileMetaInfo() throws ResourceNotFoundException, ServiceException {

        // given
        given(conversionService.convert(UPDATE_FILE_META_INFO_REQUEST_MODEL, UpdateFileMetaInfoVO.class)).willReturn(UPDATE_FILE_META_INFO_VO);
        given(fileManagementFacade.getCheckedMetaInfo(UPLOADED_FILE_VO.getPathUUID())).willReturn(Optional.of(UPLOADED_FILE_VO));

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateFileMetaInfo(UPLOADED_FILE_VO.getPathUUID(), UPDATE_FILE_META_INFO_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null, LOCATION_HEADER);
        verify(fileManagementFacade).updateMetaInfo(UPLOADED_FILE_VO.getPathUUID(), UPDATE_FILE_META_INFO_VO);
    }

    @Test
    public void shouldUpdateFileMetaInfoWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateFileMetaInfo(UPLOADED_FILE_VO.getPathUUID(), UPDATE_FILE_META_INFO_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateFileMetaInfoWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        given(conversionService.convert(UPDATE_FILE_META_INFO_REQUEST_MODEL, UpdateFileMetaInfoVO.class)).willReturn(UPDATE_FILE_META_INFO_VO);
        doThrow(ServiceException.class).when(fileManagementFacade).updateMetaInfo(UPLOADED_FILE_VO.getPathUUID(), UPDATE_FILE_META_INFO_VO);

        // when
        controller.updateFileMetaInfo(UPLOADED_FILE_VO.getPathUUID(), UPDATE_FILE_META_INFO_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldGetFileDetails() throws ResourceNotFoundException {

        // given
        given(fileManagementFacade.getCheckedMetaInfo(UPLOADED_FILE_VO.getPathUUID())).willReturn(Optional.of(UPLOADED_FILE_VO));

        // when
        ResponseEntity<FileDataModel> result = controller.getFileDetails(UPLOADED_FILE_VO.getPathUUID());

        // then
        assertResponse(result, HttpStatus.OK, FILE_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetFileDetailsWithNonExistingFile() throws ResourceNotFoundException {

        // given
        given(fileManagementFacade.getCheckedMetaInfo(UPLOADED_FILE_VO.getPathUUID())).willReturn(Optional.empty());

        // when
        controller.getFileDetails(UPLOADED_FILE_VO.getPathUUID());

        // then
        // exception expected
    }

    @Test
    public void shouldGetDirectories() {

        // given
        List<AcceptorInfoVO> acceptorInfoVOList = Collections.singletonList(ACCEPTOR_INFO_VO);
        given(fileManagementFacade.getAcceptorInfo()).willReturn(acceptorInfoVOList);
        given(conversionService.convert(acceptorInfoVOList, DirectoryListDataModel.class)).willReturn(DIRECTORY_LIST_DATA_MODEL);

        // when
        ResponseEntity<DirectoryListDataModel> result = controller.getDirectories();

        // then
        assertResponse(result, HttpStatus.OK, DIRECTORY_LIST_DATA_MODEL);
    }

    private DirectoryCreationRequestModel prepareDirectoryCreationRequestModel() {

        DirectoryCreationRequestModel requestModel = new DirectoryCreationRequestModel();
        requestModel.setParent("parent");
        requestModel.setName("directory");

        return requestModel;
    }

    private DownloadableFileWrapperVO prepareDownloadableFileWrapperVO() {
        return DownloadableFileWrapperVO.getBuilder()
                .withFileContent(FILE_CONTENT)
                .withLength(CONTENT_BYTE_ARRAY.length)
                .withMimeType(MIME_TYPE)
                .build();
    }
}