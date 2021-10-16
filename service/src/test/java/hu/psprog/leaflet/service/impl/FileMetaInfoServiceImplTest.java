package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UploadedFileDAO;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.converter.UploadedFileToUploadedFileVOConverter;
import hu.psprog.leaflet.service.converter.UploadedFileVOToUploadedFileConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FileMetaInfoServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FileMetaInfoServiceImplTest {

    private static final UploadedFile EXPECTED_UPLOADED_FILE = UploadedFile.getBuilder()
            .withId(1L)
            .build();

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

    @Test
    public void shouldThrowExceptionOnRetrieveMetaInfoWhenUUIDIsNotExisting() {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileMetaInfoService.retrieveMetaInfo(pathUUID));

        // then
        // expected exception
    }

    @Test
    public void shouldRemoveMetaInfo() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(EXPECTED_UPLOADED_FILE);

        // when
        fileMetaInfoService.removeMetaInfo(pathUUID);

        // then
        verify(uploadedFileDAO).findByPathUUID(pathUUID);
        verify(uploadedFileDAO).delete(1L);
    }

    @Test
    public void shouldStoreMetaInfo() throws ServiceException {

        // given
        given(uploadedFileVOToUploadedFileConverter.convert(any(UploadedFileVO.class))).willReturn(EXPECTED_UPLOADED_FILE);
        given(uploadedFileDAO.save(EXPECTED_UPLOADED_FILE)).willReturn(EXPECTED_UPLOADED_FILE);

        // when
        Long result = fileMetaInfoService.storeMetaInfo(UploadedFileVO.getBuilder().build());

        // then
        assertThat(result, equalTo(1L));
    }

    @Test
    public void shouldStoreMetaInfoThrowConstraintViolationException() {

        // given
        given(uploadedFileVOToUploadedFileConverter.convert(any(UploadedFileVO.class))).willReturn(EXPECTED_UPLOADED_FILE);
        doThrow(DataIntegrityViolationException.class).when(uploadedFileDAO).save(EXPECTED_UPLOADED_FILE);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> fileMetaInfoService.storeMetaInfo(UploadedFileVO.getBuilder().build()));

        // then
        // exception expected
    }

    @Test
    public void shouldStoreMetaInfoThrowServiceException() {

        // given
        given(uploadedFileVOToUploadedFileConverter.convert(any(UploadedFileVO.class))).willReturn(EXPECTED_UPLOADED_FILE);
        doThrow(IllegalArgumentException.class).when(uploadedFileDAO).save(EXPECTED_UPLOADED_FILE);

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileMetaInfoService.storeMetaInfo(UploadedFileVO.getBuilder().build()));

        // then
        // exception expected
    }

    @Test
    public void shouldStoreMetaInfoThrowEntityCreationException() {

        // given
        given(uploadedFileVOToUploadedFileConverter.convert(any(UploadedFileVO.class))).willReturn(EXPECTED_UPLOADED_FILE);
        given(uploadedFileDAO.save(EXPECTED_UPLOADED_FILE)).willReturn(null);

        // when
        Assertions.assertThrows(EntityCreationException.class, () -> fileMetaInfoService.storeMetaInfo(UploadedFileVO.getBuilder().build()));

        // then
        // exception expected
    }

    @Test
    public void shouldThrowExceptionOnRemoveMetaInfoWhenUUIDIsNotExisting() {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileMetaInfoService.removeMetaInfo(pathUUID));

        // then
        // expected exception
    }

    @Test
    public void shouldUpdateMetaInfo() throws ServiceException {

        // given
        UUID pathUUID = UUID.randomUUID();
        UploadedFile expectedUploadedFile = UploadedFile.getBuilder()
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

    @Test
    public void shouldThrowExceptionOnUpdateMetaInfoWhenUUIDIsNotExisting() {

        // given
        UUID pathUUID = UUID.randomUUID();
        given(uploadedFileDAO.findByPathUUID(any(UUID.class))).willReturn(null);
        UpdateFileMetaInfoVO updateFileMetaInfoVO = prepareUpdateFileMetaInfoVO();

        // when
        Assertions.assertThrows(ServiceException.class, () -> fileMetaInfoService.updateMetaInfo(pathUUID, updateFileMetaInfoVO));

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
        return UpdateFileMetaInfoVO.getBuilder()
                .withDescription("Updated description")
                .withOriginalFilename("renamed_image.jpg")
                .build();
    }
}
