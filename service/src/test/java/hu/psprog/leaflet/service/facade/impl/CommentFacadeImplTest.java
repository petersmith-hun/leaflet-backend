package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link CommentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentFacadeImplTest {

    private static final long REGISTERED_USER_ID = 1L;
    private static final long NEW_NO_LOGIN_USER_ID = 2L;
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
    private UserService userService;

    @Mock
    private CommentService commentService;

    @Mock
    private EntryService entryService;

    private CommentFacade commentFacade;

    @BeforeEach
    public void setup() {
        commentFacade = new CommentFacadeImpl(commentService, userService, entryService, true);
    }

    @Test
    public void testCreateOneWithRegisteredUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(REGISTERED_USER_ID);
        given(commentService.createOne(commentVO)).willReturn(COMMENT_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verifyNoInteractions(userService);
        verify(commentService).createOne(commentVO);
        verify(commentService).notifyEntryAuthor(COMMENT_ID);
    }

    @Test
    public void testCreateOneWithRegisteredUserWithoutNotification() throws ServiceException {

        // given
        commentFacade = new CommentFacadeImpl(commentService, userService, entryService, false);
        CommentVO commentVO = prepareCommentVO(REGISTERED_USER_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verifyNoInteractions(userService);
        verify(commentService).createOne(commentVO);
        verify(commentService, never()).notifyEntryAuthor(anyLong());
    }

    @Test
    public void testCreateOneWithUnknownUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        CommentVO commentVOWithUserID = prepareCommentVO(NEW_NO_LOGIN_USER_ID);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(null);
        given(userService.registerNoLogin(any(UserVO.class))).willReturn(NEW_NO_LOGIN_USER_ID);
        given(commentService.createOne(commentVOWithUserID)).willReturn(COMMENT_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService).registerNoLogin(any(UserVO.class));
        verify(commentService).createOne(commentVOWithUserID);
        verify(commentService).notifyEntryAuthor(COMMENT_ID);
    }

    @Test
    public void testCreateOneWithReturningNoLoginUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        CommentVO commentVOWithUserID = prepareCommentVO(REGISTERED_USER_ID);
        UserVO userVO = prepareUserVO(false);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(userVO);
        given(commentService.createOne(commentVOWithUserID)).willReturn(COMMENT_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService, never()).createOne(any(UserVO.class));
        verify(commentService).createOne(commentVOWithUserID);
        verify(commentService).notifyEntryAuthor(COMMENT_ID);
    }

    @Test
    public void testCreateOneByAnExistingNormalUserWithoutLogin() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        UserVO userVO = prepareUserVO(true);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(userVO);

        // when
        Assertions.assertThrows(EntityCreationException.class, () -> commentFacade.createOne(commentVO));

        // then
        // expected exception
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService, never()).createOne(any(UserVO.class));
        verify(commentService, never()).createOne(any(CommentVO.class));
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

    private CommentVO prepareCommentVO(Long userID) {
        return CommentVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(userID)
                        .withEmail(USER_EMAIL)
                        .build())
                .build();
    }

    private UserVO prepareUserVO(boolean canLogin) {
        return UserVO.getBuilder()
                .withEmail(USER_EMAIL)
                .withId(REGISTERED_USER_ID)
                .withAuthorities(AuthorityUtils.createAuthorityList(canLogin ? "USER" : "NO_LOGIN"))
                .build();
    }
}
