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
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.service.vo.mail.CommentNotification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link CommentServiceImpl} class.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    private static final EntityPageVO<CommentVO> EMPTY_ENTITY_PAGE_VO = EntityPageVO.<CommentVO>getBuilder()
            .withEntitiesOnPage(Collections.emptyList())
            .build();
    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(5L);

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

    @Mock(lenient = true)
    private Comment comment;

    @Mock
    private CommentVO commentVO;

    @Mock(lenient = true)
    private User user;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SearchHandler<CommentSearchParametersVO, Comment> searchHandler;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setup() {
        given(user.getId()).willReturn(10L);
        given(comment.getUser()).willReturn(user);
    }

    @Test
    public void testGetOneWithExistingComment() throws ServiceException {

        // given
        Long id = 1L;
        given(commentDAO.findById(id)).willReturn(Optional.of(comment));
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        CommentVO result = commentService.getOne(id);

        // then
        assertThat(result, equalTo(commentVO));
        verify(commentDAO).findById(id);
        verify(commentToCommentVOConverter).convert(comment);
    }

    @Test
    public void testGetOneWithNonExistingComment() {

        // given
        Long id = 1L;
        given(commentDAO.findById(id)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.getOne(id));

        // then
        // expected exception
        verify(commentDAO).findById(id);
        verify(commentToCommentVOConverter, never()).convert(comment);
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<CommentVO> commentVOList = List.of(commentVO);
        given(commentDAO.findAll()).willReturn(List.of(comment));
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        List<CommentVO> result = commentService.getAll();

        // then
        assertThat(result, equalTo(commentVOList));
        verify(commentDAO).findAll();
        verify(commentToCommentVOConverter).convert(comment);
    }

    @Test
    public void testGetAllWithEmptyList() {

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
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForEntry(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, ENTRY_VO);

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
        EntityPageVO<CommentVO> result = commentService.getPageOfPublicCommentsForEntry(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, ENTRY_VO);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicCommentsForEntryShouldReturnEmptyEntityPageForMissingEntry() {

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfPublicCommentsForEntry(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, null);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EMPTY_ENTITY_PAGE_VO));
        verifyNoInteractions(entryVOToEntryConverter, commentDAO);
    }

    @Test
    public void testGetPageOfCommentsForUser() {

        // given
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
        UserVO userVO = UserVO.wrapMinimumVO(6L);
        given(userVOToUserConverter.convert(userVO)).willReturn(new User());
        given(commentDAO.findByUser(any(Pageable.class), any(User.class))).willReturn(commentPage);
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForUser(1, 10, OrderDirection.ASC, CommentVO.OrderBy.CREATED, userVO);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldSearchComments() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = CommentSearchParametersVO.builder()
                .page(2)
                .limit(30)
                .orderDirection(OrderDirection.DESC)
                .orderBy(CommentVO.OrderBy.ID)
                .build();
        Pageable expectedPageable = PageRequest.of(1, 30, Sort.Direction.DESC, "id");
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(comment));
        Specification<Comment> specification = Specification.where(null);

        given(searchHandler.createSpecification(commentSearchParametersVO)).willReturn(specification);
        given(commentDAO.findAll(same(specification), eq(expectedPageable))).willReturn(commentPage);
        given(commentToCommentVOConverter.convert(any(Comment.class))).willReturn(commentVO);

        // when
        EntityPageVO<CommentVO> result = commentService.searchComments(commentSearchParametersVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getEntitiesOnPage().size(), equalTo(1));
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
        given(commentDAO.findById(commentID)).willReturn(Optional.of(comment));

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

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.updateOne(id, comment)).willReturn(Optional.of(comment));
        given(commentToCommentVOConverter.convert(comment)).willReturn(commentVO);

        // when
        CommentVO result = commentService.updateOne(id, commentVO);

        // then
        assertThat(result, equalTo(commentVO));
        verify(commentToCommentVOConverter).convert(comment);
        verify(commentVOToCommentConverter).convert(commentVO);
        verify(commentDAO).updateOne(id, comment);
    }

    @Test
    public void testUpdateOneWithFailure() {

        // given
        Long id = 1L;
        given(commentVOToCommentConverter.convert(commentVO)).willReturn(comment);
        given(commentDAO.updateOne(id, comment)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.updateOne(id, commentVO));

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

    @Test
    public void testDeleteLogicallyByEntityWithNonExistingComment() {

        // given
        Long id = 1L;
        given(commentDAO.exists(commentVO.getId())).willReturn(false);
        given(commentVO.getId()).willReturn(id);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.deleteLogicallyByEntity(commentVO));

        // then
        // expected exception
        verify(commentDAO, never()).markAsDeleted(commentVO.getId());
    }

    @Test
    public void testRestoreEntityWithExistingNonComment() {

        // given
        Long id = 1L;
        given(commentDAO.exists(commentVO.getId())).willReturn(false);
        given(commentVO.getId()).willReturn(id);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.restoreEntity(commentVO));

        // then
        // expected exception
        verify(commentDAO, never()).revertLogicalDeletion(commentVO.getId());
    }

    @Test
    public void testDeleteByIdWithNonExistingComment() {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.deleteByID(id));

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

    @Test
    public void shouldEnableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.enable(id));

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

    @Test
    public void shouldDisableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(commentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.disable(id));

        // then
        // exception expected;
    }
}
