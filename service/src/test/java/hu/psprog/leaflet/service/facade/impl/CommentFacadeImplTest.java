package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link CommentFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentFacadeImplTest {

    private static final long REGISTERED_USER_ID = 1L;
    private static final long NEW_NO_LOGIN_USER_ID = 2L;
    private static final String USER_EMAIL = "lflt123test@leaflet.dev";

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    private CommentFacade commentFacade;

    @Before
    public void setup() {
        commentFacade = new CommentFacadeImpl(commentService, userService, true);
    }

    @Test
    public void testCreateOneWithRegisteredUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(REGISTERED_USER_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verifyZeroInteractions(userService);
        verify(commentService).createOne(commentVO);
        verify(commentService).notifyEntryAuthor(anyLong());
    }

    @Test
    public void testCreateOneWithUnknownUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(null);
        given(userService.registerNoLogin(any(UserVO.class))).willReturn(NEW_NO_LOGIN_USER_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService).registerNoLogin(any(UserVO.class));
        verify(commentService).createOne(prepareCommentVO(NEW_NO_LOGIN_USER_ID));
        verify(commentService).notifyEntryAuthor(anyLong());
    }

    @Test
    public void testCreateOneWithReturningNoLoginUser() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        UserVO userVO = prepareUserVO(false);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(userVO);
        given(userService.createOne(any(UserVO.class))).willReturn(NEW_NO_LOGIN_USER_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService, never()).createOne(any(UserVO.class));
        verify(commentService).createOne(prepareCommentVO(REGISTERED_USER_ID));
        verify(commentService).notifyEntryAuthor(anyLong());
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneByAnExistingNormalUserWithoutLogin() throws ServiceException {

        // given
        CommentVO commentVO = prepareCommentVO(null);
        UserVO userVO = prepareUserVO(true);
        given(userService.silentGetUserByEmail(USER_EMAIL)).willReturn(userVO);
        given(userService.createOne(any(UserVO.class))).willReturn(NEW_NO_LOGIN_USER_ID);

        // when
        commentFacade.createOne(commentVO);

        // then
        // expected exception
        verify(userService).silentGetUserByEmail(USER_EMAIL);
        verify(userService, never()).createOne(any(UserVO.class));
        verify(commentService, never()).createOne(any(CommentVO.class));
        assertThat(commentVO.getOwner().getId(), equalTo(REGISTERED_USER_ID));
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
