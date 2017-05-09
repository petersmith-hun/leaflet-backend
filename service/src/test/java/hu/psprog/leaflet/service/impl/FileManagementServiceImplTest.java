package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.uploader.FileUploader;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

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
@RunWith(MockitoJUnitRunner.class)
public class FileManagementServiceImplTest extends TemporalFileStorageBaseTest {

    private static final String PATH = "/images/" + FILENAME;
    private static final String ORIGINAL_FILENAME = "test.jpg";
    private static final String MIME_TYPE = "image/jpg";
    private static final String DIRECTORY_TO_CREATE = "testDirectory";

    @Mock
    private FileUploader fileUploader;

    @Mock
    private FileInputVO fileInputVO;

    @InjectMocks
    private FileManagementServiceImpl fileManagementService;

    @Before
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

    @Test(expected = ServiceException.class)
    public void shouldThrowServiceExceptionOnUploadFailure() throws ServiceException {

        // given
        given(fileInputVO.getSize()).willReturn(1L);
        doThrow(FileUploadException.class).when(fileUploader).upload(fileInputVO);

        // when
        fileManagementService.upload(fileInputVO);

        // then
        // expected exception
        verify(fileUploader).upload(fileInputVO);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowServiceExceptionOnNonAcceptedFile() throws ServiceException {

        // given
        given(fileInputVO.getSize()).willReturn(1L);
        given(fileUploader.upload(fileInputVO)).willReturn(null);

        // when
        fileManagementService.upload(fileInputVO);

        // then
        // expected exception
        verify(fileUploader).upload(fileInputVO);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalArgumentExceptionOnEmptyFile() throws ServiceException {

        // given
        given(fileInputVO.getSize()).willReturn(0L);

        // when
        fileManagementService.upload(fileInputVO);

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

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnDownloadWhenFileIsMissing() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();

        // when
        fileManagementService.download(PATH);

        // then
        // expected exception
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

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnRemoveWhenFileIsMissing() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();

        // when
        fileManagementService.remove(PATH);

        // then
        // expected exception
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

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnCreateDirectoryWhenInvalidParentIsGiven() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();

        // when
        fileManagementService.createDirectory("not_existing_parent", DIRECTORY_TO_CREATE);

        // then
        // expected exception
    }

    private void prepareFileInputVo() {
        given(fileInputVO.getContentType()).willReturn(MIME_TYPE);
        given(fileInputVO.getOriginalFilename()).willReturn(ORIGINAL_FILENAME);
    }
}