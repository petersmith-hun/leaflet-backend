package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.AttachmentDAO;
import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.service.converter.AttachmentToAttachmentVOConverter;
import hu.psprog.leaflet.service.converter.AttachmentVOToAttachmentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AttachmentServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AttachmentServiceImplTest {

    @Mock
    private AttachmentDAO attachmentDAO;

    @Mock
    private AttachmentToAttachmentVOConverter attachmentToAttachmentVOConverter;

    @Mock
    private AttachmentVOToAttachmentConverter attachmentVOToAttachmentConverter;

    @Mock
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Mock
    private Attachment attachment;

    @Mock
    private AttachmentVO attachmentVO;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    public void testGetOneWithExistingAttachment() throws ServiceException {

        // given
        Long id = 1L;
        given(attachmentDAO.findOne(id)).willReturn(attachment);
        given(attachmentToAttachmentVOConverter.convert(attachment)).willReturn(attachmentVO);

        // when
        AttachmentVO result = attachmentService.getOne(id);

        // then
        assertThat(result, equalTo(attachmentVO));
        verify(attachmentDAO).findOne(id);
        verify(attachmentToAttachmentVOConverter).convert(attachment);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingAttachment() throws ServiceException {

        // given
        Long id = 1L;
        given(attachmentDAO.findOne(id)).willReturn(null);

        // when
        AttachmentVO result = attachmentService.getOne(id);

        // then
        // expected exception
        assertThat(result, equalTo(attachmentVO));
        verify(attachmentDAO).findOne(id);
        verify(attachmentToAttachmentVOConverter, never()).convert(any(Attachment.class));
    }

    @Test
    public void testGetAllWithPopulatedList() throws ServiceException {

        // given
        List<AttachmentVO> commentVOList = Arrays.asList(attachmentVO, attachmentVO, attachmentVO);
        given(attachmentDAO.findAll()).willReturn(Arrays.asList(attachment, attachment, attachment));
        given(attachmentToAttachmentVOConverter.convert(attachment)).willReturn(attachmentVO);

        // when
        List<AttachmentVO> result = attachmentService.getAll();

        // then
        assertThat(result, equalTo(commentVOList));
        verify(attachmentDAO).findAll();
        verify(attachmentToAttachmentVOConverter, times(3)).convert(attachment);
    }

    @Test
    public void testGetAllWithEmptyList() throws ServiceException {

        // given
        given(attachmentDAO.findAll()).willReturn(new LinkedList<>());

        // when
        List<AttachmentVO> result = attachmentService.getAll();

        // then
        assertThat(result, empty());
        verify(attachmentDAO).findAll();
        verify(attachmentToAttachmentVOConverter, never()).convert(any(Attachment.class));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(attachmentVOToAttachmentConverter.convert(attachmentVO)).willReturn(attachment);
        given(attachmentDAO.save(attachment)).willReturn(attachment);
        given(attachment.getId()).willReturn(1L);

        // when
        Long result = attachmentService.createOne(attachmentVO);

        // then
        assertThat(result, equalTo(attachment.getId()));
        verify(attachmentVOToAttachmentConverter).convert(attachmentVO);
        verify(attachmentDAO).save(attachment);
        verify(attachment, atLeastOnce()).getId();
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(attachmentVOToAttachmentConverter.convert(attachmentVO)).willReturn(attachment);
        given(attachmentDAO.save(attachment)).willReturn(null);
        given(attachment.getId()).willReturn(1L);

        // when
        attachmentService.createOne(attachmentVO);

        // then
        // expected exception
        verify(attachmentVOToAttachmentConverter).convert(attachmentVO);
        verify(attachmentDAO.save(attachment));
        verify(attachment, never()).getId();
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(attachmentVOToAttachmentConverter.convert(attachmentVO)).willReturn(attachment);
        given(attachmentDAO.updateOne(id, attachment)).willReturn(attachment);
        given(attachmentToAttachmentVOConverter.convert(attachment)).willReturn(attachmentVO);

        // when
        AttachmentVO result = attachmentService.updateOne(id, attachmentVO);

        // then
        assertThat(result, equalTo(attachmentVO));
        verify(attachmentToAttachmentVOConverter).convert(attachment);
        verify(attachmentVOToAttachmentConverter).convert(attachmentVO);
        verify(attachmentDAO).updateOne(id, attachment);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(attachmentVOToAttachmentConverter.convert(attachmentVO)).willReturn(attachment);
        given(attachmentDAO.updateOne(id, attachment)).willReturn(null);

        // when
        attachmentService.updateOne(id, attachmentVO);

        // then
        // expected exception
        verify(attachmentToAttachmentVOConverter, never()).convert(attachment);
        verify(attachmentVOToAttachmentConverter).convert(attachmentVO);
        verify(attachmentDAO).updateOne(id, attachment);
    }

    @Test
    public void testDeleteByEntityWithExistingComment() throws ServiceException {

        // given
        given(attachmentDAO.exists(attachmentVO.getId())).willReturn(true);

        // when
        attachmentService.deleteByEntity(attachmentVO);

        // then
        verify(attachmentDAO).delete(attachmentVO.getId());
    }

    @Test
    public void testDeleteByIdWithExistingComment() throws ServiceException {

        // given
        Long id = 1L;

        // when
        attachmentService.deleteByID(id);

        // then
        verify(attachmentDAO).delete(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntityWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(attachmentDAO.exists(attachmentVO.getId())).willReturn(false);
        given(attachmentVO.getId()).willReturn(id);

        // when
        attachmentService.deleteByEntity(attachmentVO);

        // then
        // expected exception
        verify(attachmentDAO, never()).delete(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        doThrow(IllegalArgumentException.class).when(attachmentDAO).delete(id);

        // when
        attachmentService.deleteByID(id);

        // then
        // expected exception
        verify(attachmentDAO).delete(id);
    }
}
