package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.FileMetaInfoService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.TemporalFileStorageBaseTest;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FileManagementFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FileManagementFacadeImplTest extends TemporalFileStorageBaseTest {

    private static final String ORIGINAL_FILENAME = "Original Filename.jpg";
    private static final String ACCEPTED_AS = "image/jpg";
    private static final String UPLOADED_FILE_PATH = "images/cleaned_filename.jpg";

    @Mock
    private FileMetaInfoService fileMetaInfoService;

    @Mock
    private FileManagementService fileManagementService;

    @InjectMocks
    private FileManagementFacadeImpl fileManagementFacade;

    @Test
    public void shouldUpload() throws ServiceException {

        // given
        given(fileManagementService.upload(any(FileInputVO.class))).willReturn(new UploadedFileVO());

        // when
        fileManagementFacade.upload(new FileInputVO());

        // then
        verify(fileManagementService).upload(any(FileInputVO.class));
        verify(fileMetaInfoService).storeMetaInfo(any(UploadedFileVO.class));
    }

    @Test
    public void shouldDownload() throws ServiceException, IOException {

        // given
        prepareTemporaryStorage();
        File preparedFile = prepareFileForDownload();
        UploadedFileVO uploadedFileVO = prepareUploadedFileVO();
        given(fileMetaInfoService.retrieveMetaInfo(uploadedFileVO.getPathUUID())).willReturn(uploadedFileVO);
        given(fileManagementService.download(uploadedFileVO.getPath())).willReturn(preparedFile);

        // when
        DownloadableFileWrapperVO result = fileManagementFacade.download(uploadedFileVO.getPathUUID());

        // then
        assertThat(result, notNullValue());
        assertThat(result.getOriginalFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getMimeType(), equalTo(ACCEPTED_AS));
        assertThat(result.getLength(), equalTo((long) TEST_DATA.length()));
        assertThat(result.getFileContent().getByteArray(), equalTo(TEST_DATA.getBytes()));
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnDownloadWhenFileNotExists() throws IOException, ServiceException {

        // given
        prepareTemporaryStorage();
        UploadedFileVO uploadedFileVO = prepareUploadedFileVO();
        given(fileMetaInfoService.retrieveMetaInfo(uploadedFileVO.getPathUUID())).willReturn(uploadedFileVO);
        given(fileManagementService.download(uploadedFileVO.getPath())).willReturn(new File("not_existing_file.jpg"));

        // when
        DownloadableFileWrapperVO result = fileManagementFacade.download(uploadedFileVO.getPathUUID());

        // then
        // expected exception
    }

    @Test
    public void shouldRemove() throws ServiceException {

        // given
        String path = "images/test.jpg";
        UUID pathUUID = UUID.randomUUID();
        UploadedFileVO uploadedFileVO = UploadedFileVO.getBuilder()
                .withPath(path)
                .build();
        given(fileMetaInfoService.retrieveMetaInfo(pathUUID)).willReturn(uploadedFileVO);

        // when
        fileManagementFacade.remove(pathUUID);

        // then
        verify(fileMetaInfoService).retrieveMetaInfo(pathUUID);
        verify(fileManagementService).remove(path);
        verify(fileMetaInfoService).removeMetaInfo(pathUUID);
    }

    @Test
    public void shouldCreateDirectory() throws ServiceException {

        // given
        String parent = "parentDirectory";
        String directoryName = "directoryToCreate";

        // when
        fileManagementFacade.createDirectory(parent, directoryName);

        // then
        verify(fileManagementService).createDirectory(parent, directoryName);
    }

    @Test
    public void shouldRetrieveStoredFileList() {

        // given

        // when
        fileManagementFacade.retrieveStoredFileList();

        // then
        verify(fileMetaInfoService).getUploadedFiles();
    }

    @Test
    public void shouldReturnCheckedMetaInfo() throws IOException, ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFileVO uploadedFileVO = prepareUploadedFileVO();
        given(fileMetaInfoService.retrieveMetaInfo(pathUUID)).willReturn(uploadedFileVO);
        given(fileManagementService.exists(uploadedFileVO.getPath())).willReturn(true);

        // when
        Optional<UploadedFileVO> result = fileManagementFacade.getCheckedMetaInfo(pathUUID);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(uploadedFileVO));
    }

    @Test
    public void shouldReturnEmptyOptionalIfMetaInfoDoesNotExist() throws IOException, ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        doThrow(ServiceException.class).when(fileMetaInfoService).retrieveMetaInfo(pathUUID);

        // when
        Optional<UploadedFileVO> result = fileManagementFacade.getCheckedMetaInfo(pathUUID);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyOptionalIfFileDoesNotExist() throws IOException, ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFileVO uploadedFileVO = prepareUploadedFileVO();
        given(fileMetaInfoService.retrieveMetaInfo(pathUUID)).willReturn(uploadedFileVO);
        given(fileManagementService.exists(uploadedFileVO.getPath())).willReturn(false);

        // when
        Optional<UploadedFileVO> result = fileManagementFacade.getCheckedMetaInfo(pathUUID);

        // then
        assertThat(result.isPresent(), is(false));
    }

    private UploadedFileVO prepareUploadedFileVO() {
        return UploadedFileVO.getBuilder()
                .withPathUUID(UUID.randomUUID())
                .withOriginalFilename(ORIGINAL_FILENAME)
                .withAcceptedAs(ACCEPTED_AS)
                .withPath(UPLOADED_FILE_PATH)
                .build();
    }
}
