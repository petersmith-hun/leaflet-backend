package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.uploader.FileUploader;
import hu.psprog.leaflet.service.impl.uploader.acceptor.ImageUploadAcceptor;
import hu.psprog.leaflet.service.impl.uploader.acceptor.UploadAcceptor;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests {@link FileManagementServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FileManagementServiceImplTest extends TemporalFileStorageBaseTest {

    private static final String PATH = "/images/" + FILENAME;
    private static final String ORIGINAL_FILENAME = "test.jpg";
    private static final String MIME_TYPE = "image/jpg";
    private static final String DIRECTORY_TO_CREATE = "testDirectory";

    @Mock(lenient = true)
    private FileUploader fileUploader;

    @Mock(lenient = true)
    private FileInputVO fileInputVO;

    @Mock(lenient = true)
    private List<UploadAcceptor> uploadAcceptors;

    @InjectMocks
    private FileManagementServiceImpl fileManagementService;

    @BeforeEach
    public void setup() {
        prepareFileInputVo();
    }

    @Test
    public void shouldUploadFile() throws ServiceException {

        // given
        given(fileInputVO.getSize()).willReturn(1L);
        given(fileUploader.upload(fileInputVO)).willReturn(new UploadedFileVO());

        // when
        UploadedFileVO result = fileManagementService.upload(fileInputVO);

        // then
        assertThat(result, notNullValue());
        verify(fileUploader).upload(fileInputVO);
    }

    @Test
    public void shouldThrowServiceExceptionOnUploadFailure() {

        // given
        given(fileInputVO.getSize()).willReturn(1L);
        doThrow(FileUploadException.class).when(fileUploader).upload(fileInputVO);

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.upload(fileInputVO));

        // then
        // expected exception
        verify(fileUploader).upload(fileInputVO);
    }

    @Test
    public void shouldThrowServiceExceptionOnNonAcceptedFile() {

        // given
        given(fileInputVO.getSize()).willReturn(1L);
        given(fileUploader.upload(fileInputVO)).willReturn(null);

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.upload(fileInputVO));

        // then
        // expected exception
        verify(fileUploader).upload(fileInputVO);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionOnEmptyFile() {

        // given
        given(fileInputVO.getSize()).willReturn(0L);

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> fileManagementService.upload(fileInputVO));

        // then
        // expected exception
        verify(fileUploader, never()).upload(fileInputVO);
    }

    @Test
    public void shouldDownloadFile() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        File expectedFile = prepareFileForDownload();

        // when
        File result = fileManagementService.download(PATH);

        // then
        assertThat(result, notNullValue());
        assertThat(result.canRead(), is(true));
        assertThat(result.length(), equalTo((long) TEST_DATA.length()));
        assertThat(result, equalTo(expectedFile));
    }

    @Test
    public void shouldThrowExceptionOnDownloadWhenFileIsMissing() throws IOException {

        // given
        prepareTemporaryStorage();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.download(PATH));

        // then
        // expected exception
    }

    @Test
    public void shouldThrowServiceExceptionOnDownloadWhenInvalidPathExceptionIsThrown() throws IOException {

        // given
        prepareTemporaryStorage();
        doThrow(InvalidPathException.class).when(fileStorage).getAbsolutePath();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.download(PATH));

        // then
        // exception expected
    }

    @Test
    public void shouldRemoveFile() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        File preparedFile = prepareFileForDownload();
        // we need to assert that the prepared file is accessible before removal!
        Assert.state(preparedFile.canRead());

        // when
        fileManagementService.remove(PATH);

        // then
        assertThat(preparedFile.canRead(), is(false));
    }

    @Test
    public void shouldThrowExceptionOnRemoveWhenFileIsMissing() throws IOException {

        // given
        prepareTemporaryStorage();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.remove(PATH));

        // then
        // expected exception
    }

    @Test
    public void shouldThrowServiceExceptionOnRemoveWhenInvalidPathExceptionIsThrown() throws IOException {

        // given
        prepareTemporaryStorage();
        doThrow(InvalidPathException.class).when(fileStorage).getAbsolutePath();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.remove(PATH));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateDirectoryUnderAcceptorRoot() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        File file = new File(getImagesRootFolder(), DIRECTORY_TO_CREATE);

        // when
        fileManagementService.createDirectory(IMAGES_FOLDER, DIRECTORY_TO_CREATE);

        // then
        assertThat(file.exists(), is(true));
        assertThat(file.isDirectory(), is(true));
    }

    @Test
    public void shouldCreateDirectoryUnderSubDirectory() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        File file = new File(getImagesSubFolder(), DIRECTORY_TO_CREATE);
        String subFolderPath = IMAGES_FOLDER + '/' + SUBFOLDER;

        // when
        fileManagementService.createDirectory(subFolderPath, DIRECTORY_TO_CREATE);

        // then
        assertThat(file.exists(), is(true));
        assertThat(file.isDirectory(), is(true));
    }

    @Test
    public void shouldThrowExceptionOnCreateDirectoryWhenInvalidParentIsGiven() throws IOException {

        // given
        prepareTemporaryStorage();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.createDirectory("not_existing_parent", DIRECTORY_TO_CREATE));

        // then
        // expected exception
    }

    @Test
    public void shouldThrowServiceExceptionOnCreateDirectoryWhenInvalidPathExceptionIsThrown() throws IOException {

        // given
        prepareTemporaryStorage();
        doThrow(InvalidPathException.class).when(fileStorage).getAbsolutePath();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.createDirectory(IMAGES_FOLDER, DIRECTORY_TO_CREATE));

        // then
        // exception expected
    }

    @Test
    public void shouldExistsReturnTrue() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        prepareFileForDownload();

        // when
        boolean result = fileManagementService.exists(PATH);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldExistsReturnFalse() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();

        // when
        boolean result = fileManagementService.exists(PATH);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldThrowServiceExceptionOnExistsWhenInvalidPathExceptionIsThrown() throws IOException {

        // given
        prepareTemporaryStorage();
        doThrow(InvalidPathException.class).when(fileStorage).getAbsolutePath();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileManagementService.exists(PATH));

        // then
        // exception expected
    }

    @Test
    public void shouldGetAcceptorInfo() throws IOException {

        // given
        prepareTemporaryStorage();
        AcceptorInfoVO expectedAcceptorInfoVO = AcceptorInfoVO.getBuilder()
                .withId("IMAGE")
                .withRootDirectoryName("images")
                .withAcceptableMimeTypes(Collections.singletonList("image/*"))
                .withChildrenDirectories(Arrays.asList("", "test"))
                .build();
        List<UploadAcceptor> acceptors = Collections.singletonList(new ImageUploadAcceptor());
        given(uploadAcceptors.stream()).willReturn(acceptors.stream());

        // when
        List<AcceptorInfoVO> result = fileManagementService.getAcceptorInfo();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0), equalTo(expectedAcceptorInfoVO));
    }

    @Test
    public void shouldGetAcceptorInfoReturnWithEmptyChildrenListOnException() throws IOException {

        // given
        prepareTemporaryStorage();
        AcceptorInfoVO expectedAcceptorInfoVO = AcceptorInfoVO.getBuilder()
                .withId("IMAGE")
                .withRootDirectoryName("images")
                .withAcceptableMimeTypes(Collections.singletonList("image/*"))
                .withChildrenDirectories(Collections.emptyList())
                .build();
        List<UploadAcceptor> acceptors = Collections.singletonList(new ImageUploadAcceptor());
        given(uploadAcceptors.stream()).willReturn(acceptors.stream());
        given(fileStorage.getAbsolutePath()).willReturn("/non/existing/path");

        // when
        List<AcceptorInfoVO> result = fileManagementService.getAcceptorInfo();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0), equalTo(expectedAcceptorInfoVO));
    }

    private void prepareFileInputVo() {
        given(fileInputVO.getContentType()).willReturn(MIME_TYPE);
        given(fileInputVO.getOriginalFilename()).willReturn(ORIGINAL_FILENAME);
    }
}
