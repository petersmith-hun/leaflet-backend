package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentSearchParametersVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentFacadeImplTest {

    private static final long REGISTERED_USER_ID = 1L;
    private static final String USER_EMAIL = "lflt123test@leaflet.dev";
    private static final long COMMENT_ID = 5L;
    private static final long ENTRY_ID = 4L;
    private static final String ENTRY_LINK = "entry-link";
    private static final CommentVO COMMENT_VO = CommentVO.wrapMinimumVO(COMMENT_ID);
    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(ENTRY_ID);
    private static final UserVO USER_VO = UserVO.wrapMinimumVO(REGISTERED_USER_ID);
    private static final int PAGE = 1;
    private static final int LIMIT = 10;
    private static final String DIRECTION = "DESC";
    private static final String ORDER_BY = "ID";
    private static final String NON_EXISTING = "non-existing";

    @Mock
    private CommentService commentService;

    @Mock
    private EntryService entryService;

    private CommentFacade commentFacade;

    @BeforeEach
    public void setup() {
        commentFacade = new CommentFacadeImpl(commentService, entryService, true);
    }

    @Test
    public void testCreateOneWithRegisteredUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO();
        given(commentService.createOne(commentVO)).willReturn(COMMENT_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(commentService).createOne(commentVO);
        verify(commentService).notifyEntryAuthor(COMMENT_ID);
    }

    @Test
    public void testCreateOneWithRegisteredUserWithoutNotification() throws ServiceException {

        // given
        commentFacade = new CommentFacadeImpl(commentService, entryService, false);
        CommentVO commentVO = prepareCommentVO();

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(commentService).createOne(commentVO);
        verify(commentService, never()).notifyEntryAuthor(anyLong());
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        commentFacade.deletePermanently(COMMENT_ID);

        // then
        verify(commentService).deleteByID(COMMENT_ID);
    }

    @Test
    public void shouldDeleteLogically() throws ServiceException {

        // given
        given(commentService.getOne(COMMENT_ID)).willReturn(COMMENT_VO);

        // when
        commentFacade.deleteLogically(COMMENT_ID);

        // then
        verify(commentService).deleteLogicallyByEntity(COMMENT_VO);
    }

    @Test
    public void shouldRestoreEntity() throws ServiceException {

        // given
        given(commentService.getOne(COMMENT_ID)).willReturn(COMMENT_VO);

        // when
        commentFacade.restoreEntity(COMMENT_ID);

        // then
        verify(commentService).restoreEntity(COMMENT_VO);
    }

    @Test
    public void shouldChangeStatusToEnabled() throws ServiceException {

        // given
        given(commentService.getOne(COMMENT_ID)).willReturn(COMMENT_VO);

        // when
        commentFacade.changeStatus(COMMENT_ID);

        // then
        verify(commentService).enable(COMMENT_ID);
    }

    @Test
    public void shouldChangeStatusToDisabled() throws ServiceException {

        // given
        given(commentService.getOne(COMMENT_ID)).willReturn(CommentVO.getBuilder()
                .withEnabled(true)
                .build());

        // when
        commentFacade.changeStatus(COMMENT_ID);

        // then
        verify(commentService).disable(COMMENT_ID);
    }

    @Test
    public void shouldUpdateOne() throws ServiceException {

        // when
        commentFacade.updateOne(COMMENT_ID, COMMENT_VO);

        // then
        verify(commentService).updateOne(COMMENT_ID, COMMENT_VO);
        verify(commentService).getOne(COMMENT_ID);
    }

    @Test
    public void shouldGetPageOfCommentsForEntry() {

        // when
        commentFacade.getPageOfCommentsForEntry(ENTRY_ID, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(commentService).getPageOfCommentsForEntry(PAGE, LIMIT, OrderDirection.DESC, CommentVO.OrderBy.ID, ENTRY_VO);
    }

    @Test
    public void shouldGetPageOfCommentsForEntryWithPagingFallbackParameters() {

        // when
        commentFacade.getPageOfCommentsForEntry(ENTRY_ID, PAGE, LIMIT, NON_EXISTING, NON_EXISTING);

        // then
        verify(commentService).getPageOfCommentsForEntry(PAGE, LIMIT, OrderDirection.ASC, CommentVO.OrderBy.CREATED, ENTRY_VO);
    }

    @Test
    public void shouldGetPageOfPublicCommentsForEntry() throws EntityNotFoundException {

        // given
        given(entryService.findByLink(ENTRY_LINK)).willReturn(ENTRY_VO);

        // when
        commentFacade.getPageOfPublicCommentsForEntry(ENTRY_LINK, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(commentService).getPageOfPublicCommentsForEntry(PAGE, LIMIT, OrderDirection.DESC, CommentVO.OrderBy.ID, ENTRY_VO);
    }

    @Test
    public void shouldGetPageOfPublicCommentsForEntryHandleException() throws EntityNotFoundException {

        // given
        doThrow(EntityNotFoundException.class).when(entryService).findByLink(ENTRY_LINK);

        // when
        commentFacade.getPageOfPublicCommentsForEntry(ENTRY_LINK, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(commentService).getPageOfPublicCommentsForEntry(PAGE, LIMIT, OrderDirection.DESC, CommentVO.OrderBy.ID, null);
    }

    @Test
    public void shouldGetPageOfCommentsForUser() {

        // when
        commentFacade.getPageOfCommentsForUser(REGISTERED_USER_ID, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(commentService).getPageOfCommentsForUser(PAGE, LIMIT, OrderDirection.DESC, CommentVO.OrderBy.ID, USER_VO);
    }

    @Test
    public void shouldGetOne() throws ServiceException {

        // when
        commentFacade.getOne(COMMENT_ID);

        // then
        verify(commentService).getOne(COMMENT_ID);
    }

    @Test
    public void shouldGetAll() {

        // when
        commentFacade.getAll();

        // then
        verify(commentService).getAll();
    }

    @Test
    public void shouldSearchCommentsDelegateRequestImmediatelyToService() {

        // given
        CommentSearchParametersVO commentSearchParametersVO = CommentSearchParametersVO.builder().build();

        // when
        commentFacade.searchComments(commentSearchParametersVO);

        // then
        verify(commentService).searchComments(commentSearchParametersVO);
    }

    private CommentVO prepareCommentVO() {
        return CommentVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(REGISTERED_USER_ID)
                        .withEmail(USER_EMAIL)
                        .build())
                .build();
    }
}
