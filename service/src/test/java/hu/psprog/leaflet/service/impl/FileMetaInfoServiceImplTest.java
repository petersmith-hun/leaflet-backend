package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.converter.UploadedFileToUploadedFileVOConverter;
import hu.psprog.leaflet.service.converter.UploadedFileVOToUploadedFileConverter;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FileMetaInfoServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FileMetaInfoServiceImplTest {

    @Mock
    private UploadedFileDAO uploadedFileDAO;

    @Mock
    private UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter;

    @Mock
    private UploadedFileVOToUploadedFileConverter uploadedFileVOToUploadedFileConverter;

    @InjectMocks
    private FileMetaInfoServiceImpl fileMetaInfoService;

    @Test
    public void shouldRetrieveMetaInfo() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFile expectedUploadedFile = new UploadedFile();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(expectedUploadedFile);

        // when
        fileMetaInfoService.retrieveMetaInfo(pathUUID);

        // then
        verify(uploadedFileDAO).findByPathUUID(pathUUID);
        verify(uploadedFileToUploadedFileVOConverter).convert(expectedUploadedFile);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnRetrieveMetaInfoWhenUUIDIsNotExisting() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);

        // when
        fileMetaInfoService.retrieveMetaInfo(pathUUID);

        // then
        // expected exception
    }

    @Test
    public void shouldRemoveMetaInfo() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFile expectedUploadedFile = UploadedFile.Builder.getBuilder()
                .withId(1L)
                .build();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(expectedUploadedFile);

        // when
        fileMetaInfoService.removeMetaInfo(pathUUID);

        // then
        verify(uploadedFileDAO).findByPathUUID(pathUUID);
        verify(uploadedFileDAO).delete(1L);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnRemoveMetaInfoWhenUUIDIsNotExisting() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);

        // when
        fileMetaInfoService.removeMetaInfo(pathUUID);

        // then
        // expected exception
    }

    @Test
    public void shouldUpdateMetaInfo() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFile expectedUploadedFile = UploadedFile.Builder.getBuilder()
                .withId(1L)
                .build();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(expectedUploadedFile);
        UpdateFileMetaInfoVO updateFileMetaInfoVO = prepareUpdateFileMetaInfoVO();

        // when
        fileMetaInfoService.updateMetaInfo(pathUUID, updateFileMetaInfoVO);

        // then
        assertThat(expectedUploadedFile.getOriginalFilename(), equalTo(updateFileMetaInfoVO.getOriginalFilename()));
        assertThat(expectedUploadedFile.getDescription(), equalTo(updateFileMetaInfoVO.getDescription()));
        verify(uploadedFileDAO).findByPathUUID(pathUUID);
        verify(uploadedFileDAO).updateOne(1L, expectedUploadedFile);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionOnUpdateMetaInfoWhenUUIDIsNotExisting() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);
        UpdateFileMetaInfoVO updateFileMetaInfoVO = prepareUpdateFileMetaInfoVO();

        // when
        fileMetaInfoService.updateMetaInfo(pathUUID, updateFileMetaInfoVO);

        // then
        // expected exception
    }

    @Test
    public void shouldGetUploadedFiles() {

        // given
        List<UploadedFile> uploadedFileList = Arrays.asList(new UploadedFile(), new UploadedFile(), new UploadedFile());
        given(uploadedFileDAO.findAll()).willReturn(uploadedFileList);
        given(uploadedFileToUploadedFileVOConverter.convert(any(UploadedFile.class))).willReturn(new UploadedFileVO());

        // when
        List<UploadedFileVO> result = fileMetaInfoService.getUploadedFiles();

        // then
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), equalTo(3));
        verify(uploadedFileDAO).findAll();
        verify(uploadedFileToUploadedFileVOConverter, times(3)).convert(any(UploadedFile.class));
    }

    private UpdateFileMetaInfoVO prepareUpdateFileMetaInfoVO() {
        return UpdateFileMetaInfoVO.Builder.getBuilder()
                .withDescription("Updated description")
                .withOriginalFilename("renamed_image.jpg")
                .build();
    }
}
