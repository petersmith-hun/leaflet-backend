package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.ClearStorage;
import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.file.DirectoryCreationRequestModel;
import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.api.rest.request.file.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.FileBridgeService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Acceptance tests for {@code /files} endpoints.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
@Disabled
public class FilesControllerAcceptanceTest extends AbstractTempFileHandlingParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_FILES = 3;
    private static final String ACCEPTED_AS_IMAGE_JPEG = "image/jpeg";
    private static final String IMAGES_ROOT_DIRECTORY = "images";
    private static final String CONTROL_DIRECTORIES = "directories";
    private static final String CONTROL_FILE_3 = "file-3";
    private static final UUID CONTROL_FILE_UUID = UUID.fromString("058c9d47-6ce4-3e48-9c44-35bb9c74b378");
    private static final String SUB_FOLDER = "test_sub";
    private static final String DESCRIPTION = "Description of uploaded file";
    private static final String MOCK_FILE_NAME = "Upload test";
    private static final String MOCK_FILE_ORIGINAL_FILENAME = "upload-test.jpg";
    private static final byte[] MOCK_FILE_CONTENT = "content".getBytes();
    private static final String CONTROL_FILE_UPLOAD = "file-upload";
    private static final UUID CONTROL_FILE_UPLOAD_UUID = UUID.fromString("13bccef4-dcbd-3086-b8c5-b7fae3de9d38");
    private static final String UPLOADED_MOCK_FILE_PATH = "images/test_sub/" + MOCK_FILE_ORIGINAL_FILENAME;
    private static final String CREATED_DIRECTORY_NAME = "created-directory";
    private static final String CREATED_DIRECTORY_PATH = "images/created-directory";

    @Autowired
    private FileBridgeService fileBridgeService;

    @Test
    public void shouldGetFiles() throws CommunicationFailureException {

        // when
        FileListDataModel result = fileBridgeService.getUploadedFiles();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getFiles().size(), equalTo(NUMBER_OF_ALL_FILES));
        assertThat(result.getFiles().stream()
                .allMatch(fileDataModel -> ACCEPTED_AS_IMAGE_JPEG.equals(fileDataModel.getAcceptedAs())), is(true));
        assertThat(result.getFiles().stream()
                .filter(fileDataModel -> fileDataModel.getPath().startsWith("images/"))
                .count(), equalTo(3L));
        assertThat(result.getFiles().stream()
                .filter(fileDataModel -> fileDataModel.getPath().startsWith("images/test_sub/"))
                .count(), equalTo(1L));
        result.getFiles().forEach(fileDataModel -> {

            // path UUID must be extractable from reference, and it must always be between two slashes
            try {
                UUID.fromString(fileDataModel.getReference().split("/")[1]);
            } catch (Exception e) {
                fail("Failed to extract path UUID from reference string.");
            }
        });
    }

    @Test
    public void shouldGetDirectories() throws CommunicationFailureException {

        // given
        DirectoryListDataModel control = getControl(CONTROL_DIRECTORIES, DirectoryListDataModel.class);

        // when
        DirectoryListDataModel result = fileBridgeService.getDirectories();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    public void shouldGetFileMetaData() throws CommunicationFailureException {

        // given
        FileDataModel control = getControl(CONTROL_FILE_3, FileDataModel.class);

        // when
        FileDataModel result = fileBridgeService.getFileDetails(CONTROL_FILE_UUID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateFileMetaInfo() throws CommunicationFailureException {

        // given
        UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel = new UpdateFileMetaInfoRequestModel();
        updateFileMetaInfoRequestModel.setOriginalFilename("updated-filename");
        updateFileMetaInfoRequestModel.setDescription("Updated file description");

        // when
        fileBridgeService.updateFileMetaInfo(CONTROL_FILE_UUID, updateFileMetaInfoRequestModel);

        // then
        FileDataModel current = fileBridgeService.getFileDetails(CONTROL_FILE_UUID);
        assertThat(current.getOriginalFilename(), equalTo(updateFileMetaInfoRequestModel.getOriginalFilename()));
        assertThat(current.getDescription(), equalTo(updateFileMetaInfoRequestModel.getDescription()));
    }

    @Test
    @ResetDatabase
    @ClearStorage(path = UPLOADED_MOCK_FILE_PATH)
    public void shouldUploadFile() throws CommunicationFailureException {

        // given
        FileUploadRequestModel fileUploadRequestModel = prepareFileUploadRequestModel();

        // when
        FileDataModel result = fileBridgeService.uploadFile(fileUploadRequestModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(getControl(CONTROL_FILE_UPLOAD, FileDataModel.class)));
    }

    @Test
    @ResetDatabase
    @ClearStorage(path = UPLOADED_MOCK_FILE_PATH)
    public void shouldDownloadFile() throws CommunicationFailureException {

        // given
        fileBridgeService.uploadFile(prepareFileUploadRequestModel());

        // when
        InputStream result = fileBridgeService.downloadFile(CONTROL_FILE_UPLOAD_UUID, MOCK_FILE_ORIGINAL_FILENAME);

        // then
        assertThat(result, notNullValue());
        assertThat(getResultAsByteArray(result), equalTo(MOCK_FILE_CONTENT));
    }

    @Test
    @ClearStorage(path = CREATED_DIRECTORY_PATH)
    public void shouldCreateDirectory() throws CommunicationFailureException {

        // given
        DirectoryCreationRequestModel directoryCreationRequestModel = new DirectoryCreationRequestModel();
        directoryCreationRequestModel.setParent(IMAGES_ROOT_DIRECTORY);
        directoryCreationRequestModel.setName(CREATED_DIRECTORY_NAME);

        // when
        fileBridgeService.createDirectory(directoryCreationRequestModel);

        // then
        assertThat(fileBridgeService.getDirectories().getAcceptors().get(0).getChildren().stream()
                .anyMatch(CREATED_DIRECTORY_NAME::equals), is(true));
    }

    @Test
    @ResetDatabase
    @ClearStorage(path = UPLOADED_MOCK_FILE_PATH)
    public void shouldDeleteFile() throws CommunicationFailureException {

        // given
        fileBridgeService.uploadFile(prepareFileUploadRequestModel());

        // when
        fileBridgeService.deleteFile(CONTROL_FILE_UPLOAD_UUID);

        // then
        try {
            fileBridgeService.downloadFile(CONTROL_FILE_UPLOAD_UUID, MOCK_FILE_ORIGINAL_FILENAME);
            fail("Test should have thrown ResourceNotFoundException");
        } catch (ResourceNotFoundException e) {
            // this call should cause ResourceNotFoundException
        }
    }

    private byte[] getResultAsByteArray(InputStream result) {

        byte[] content = new byte[0];
        try {
            content = IOUtils.toByteArray(result);
        } catch (IOException e) {
            fail("Failed to convert result InputStream to byte array.");
        }

        return content;
    }

    private FileUploadRequestModel prepareFileUploadRequestModel() {

        FileUploadRequestModel fileUploadRequestModel = new FileUploadRequestModel();
        fileUploadRequestModel.setSubFolder(SUB_FOLDER);
        fileUploadRequestModel.setDescription(DESCRIPTION);
        fileUploadRequestModel.setInputFile(prepareMultipartFile());

        return fileUploadRequestModel;
    }

    private MultipartFile prepareMultipartFile() {
        return new MockMultipartFile(MOCK_FILE_NAME, MOCK_FILE_ORIGINAL_FILENAME, ACCEPTED_AS_IMAGE_JPEG, MOCK_FILE_CONTENT);
    }
}
