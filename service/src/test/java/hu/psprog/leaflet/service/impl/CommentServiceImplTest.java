package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.service.converter.CommentToCommentVOConverter;
import hu.psprog.leaflet.service.converter.CommentVOToCommentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CommentVO;
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
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    @Mock
    private CommentDAO commentDAO;

    @Mock
    private CommentToCommentVOConverter commentToCommentVOConverter;

    @Mock
    private CommentVOToCommentConverter commentVOToCommentConverter;

    @Mock
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Mock
    private UserVOToUserConverter userVOToUserConverter;

    @Mock
    private Comment comment;

    @Mock
    private CommentVO commentVO;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testGetOneWithExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.findOne(id)).willReturn(comment);
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        CommentVO result = commentService.getOne(id);

        // then
        assertThat(result, equalTo(commentVO));
        verify(commentDAO).findOne(id);
        verify(commentToCommentVOConverter).convert(comment);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.findOne(id)).willReturn(null);

        // when
        CommentVO result = commentService.getOne(id);

        // then
        // expected exception
        assertThat(result, equalTo(commentVO));
        verify(commentDAO).findOne(id);
        verify(commentToCommentVOConverter, never()).convert(comment);
    }

    @Test
    public void testGetAllWithPopulatedList() throws ServiceException {

        // given
        List<CommentVO> commentVOList = Arrays.asList(commentVO);
        given(commentDAO.findAll()).willReturn(Arrays.asList(comment));
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        List<CommentVO> result = commentService.getAll();

        // then
        assertThat(result, equalTo(commentVOList));
        verify(commentDAO).findAll();
        verify(commentToCommentVOConverter).convert(comment);
    }

    @Test
    public void testGetAllWithEmptyList() throws ServiceException {

        // given
        given(commentDAO.findAll()).willReturn(new LinkedList<>());

        // when
        List<CommentVO> result = commentService.getAll();

        // then
        assertThat(result, empty());
        verify(commentDAO).findAll();
        verify(commentToCommentVOConverter, never()).convert(any(Comment.class));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.save(comment)).willReturn(comment);
        given(comment.getId()).willReturn(1L);

        // when
        Long result = commentService.createOne(commentVO);

        // then
        assertThat(result, equalTo(comment.getId()));
        verify(commentVOToCommentConverter).convert(commentVO);
        verify(commentDAO).save(comment);
        verify(comment, atLeastOnce()).getId();
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.save(comment)).willReturn(null);
        given(comment.getId()).willReturn(1L);

        // when
        commentService.createOne(commentVO);

        // then
        // expected exception
        verify(commentVOToCommentConverter).convert(commentVO);
        verify(commentDAO.save(comment));
        verify(comment, never()).getId();
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.updateOne(id, comment)).willReturn(comment);
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        CommentVO result = commentService.updateOne(id, commentVO);

        // then
        assertThat(result, equalTo(commentVO));
        verify(commentToCommentVOConverter).convert(comment);
        verify(commentVOToCommentConverter).convert(commentVO);
        verify(commentDAO).updateOne(id, comment);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.updateOne(id, comment)).willReturn(null);

        // when
        commentService.updateOne(id, commentVO);

        // then
        // expected exception
        verify(commentToCommentVOConverter, never()).convert(comment);
        verify(commentVOToCommentConverter).convert(commentVO);
        verify(commentDAO).updateOne(id, comment);
    }

    @Test
    public void testDeleteByEntityWithExistingComment() throws ServiceException {

        // given
        given(commentDAO.exists(commentVO.getId())).willReturn(true);

        // when
        commentService.deleteByEntity(commentVO);

        // then
        verify(commentDAO).delete(commentVO.getId());
    }

    @Test
    public void testDeleteByIdWithExistingComment() throws ServiceException {

        // given
        Long id = 1L;

        // when
        commentService.deleteByID(id);

        // then
        verify(commentDAO).delete(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntityWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.exists(commentVO.getId())).willReturn(false);
        given(commentVO.getId()).willReturn(id);

        // when
        commentService.deleteByEntity(commentVO);

        // then
        // expected exception
        verify(commentDAO, never()).delete(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        doThrow(IllegalArgumentException.class).when(commentDAO).delete(id);

        // when
        commentService.deleteByID(id);

        // then
        // expected exception
        verify(commentDAO).delete(id);
    }
}
