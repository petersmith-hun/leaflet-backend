package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CommentToCommentVOConverter;
import hu.psprog.leaflet.service.converter.CommentVOToCommentConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    @Mock(lenient = true)
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

    @Mock
    private User user;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Before
    public void setup() {
        given(user.getId()).willReturn(10L);
        given(comment.getUser()).willReturn(user);
    }

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
    public void testGetPageOfCommentsForEntry() {

        // given
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
        given(entryVOToEntryConverter.convert(any(EntryVO.class))).willReturn(new Entry());
        given(commentDAO.findByEntry(any(Pageable.class), any(Entry.class))).willReturn(commentPage);

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForEntry(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, new EntryVO());

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicCommentsForEntry() {

        // given
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
        given(entryVOToEntryConverter.convert(any(EntryVO.class))).willReturn(new Entry());
        given(commentDAO.findByEntry(any(Specification.class), any(Pageable.class), any(Entry.class))).willReturn(commentPage);

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfPublicCommentsForEntry(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, new EntryVO());

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfCommentsForUser() {

        // given
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
        UserVO userVO = new UserVO();
        given(userVOToUserConverter.convert(userVO)).willReturn(new User());
        given(commentDAO.findByUser(any(Pageable.class), any(User.class))).willReturn(commentPage);
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForUser(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, userVO);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testCount() {

        // given
        Long count = 5L;
        given(commentDAO.count()).willReturn(count);

        // when
        Long result = commentService.count();

        // then
        assertThat(result, equalTo(count));
    }

    @Test
    public void testNotifyEntryAuthor() {

        // given
        Comment comment = Comment.getBuilder()
                .withUser(User.getBuilder()
                        .withUsername("commenter-username")
                        .withEmail("commenter-email")
                        .build())
                .withContent("content")
                .withEntry(Entry.getBuilder()
                        .withTitle("title")
                        .withUser(User.getBuilder()
                                .withEmail("author-email")
                                .withUsername("author-username")
                                .build())
                        .build())
                .build();
        Long commentID = 1L;
        given(commentDAO.findOne(commentID)).willReturn(comment);

        // when
        commentService.notifyEntryAuthor(commentID);

        // then
        verify(notificationService).commentNotification(CommentNotification.getBuilder()
                .withUsername(comment.getUser().getUsername())
                .withEmail(comment.getUser().getEmail())
                .withContent(comment.getContent())
                .withEntryTitle(comment.getEntry().getTitle())
                .withAuthorEmail(comment.getEntry().getUser().getEmail())
                .withAuthorName(comment.getEntry().getUser().getUsername())
                .build());
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

        // when
        try {
            commentService.createOne(commentVO);
        } finally {

            // then
            // expected exception
            verify(commentVOToCommentConverter).convert(commentVO);
            verify(commentDAO).save(comment);
            verify(comment, never()).getId();
        }
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
    public void testDeleteByIdWithExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(true);

        // when
        commentService.deleteByID(id);

        // then
        verify(commentDAO).delete(id);
    }

    @Test
    public void testDeleteLogicallyByEntityWithExistingComment() throws ServiceException {

        // given
        given(commentDAO.exists(commentVO.getId())).willReturn(true);

        // when
        commentService.deleteLogicallyByEntity(commentVO);

        // then
        verify(commentDAO).markAsDeleted(commentVO.getId());
    }

    @Test
    public void testRestoreEntityWithExistingComment() throws ServiceException {

        // given
        given(commentDAO.exists(commentVO.getId())).willReturn(true);

        // when
        commentService.restoreEntity(commentVO);

        // then
        verify(commentDAO).revertLogicalDeletion(commentVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteLogicallyByEntityWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.exists(commentVO.getId())).willReturn(false);
        given(commentVO.getId()).willReturn(id);

        // when
        commentService.deleteLogicallyByEntity(commentVO);

        // then
        // expected exception
        verify(commentDAO, never()).markAsDeleted(commentVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRestoreEntityWithExistingNonComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.exists(commentVO.getId())).willReturn(false);
        given(commentVO.getId()).willReturn(id);

        // when
        commentService.restoreEntity(commentVO);

        // then
        // expected exception
        verify(commentDAO, never()).revertLogicalDeletion(commentVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIdWithNonExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        commentService.deleteByID(id);

        // then
        // expected exception
    }

    @Test
    public void shouldEnable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(true);

        // when
        commentService.enable(id);

        // then
        verify(commentDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldEnableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        commentService.enable(id);

        // then
        // exception expected;
    }

    @Test
    public void shouldDisable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(true);

        // when
        commentService.disable(id);

        // then
        verify(commentDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldDisableThrowEntityNotFoundException() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        commentService.disable(id);

        // then
        // exception expected;
    }
}
