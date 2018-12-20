package hu.psprog.leaflet.service.security.evaluator;

import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.JWTPayload;
import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link OwnershipEvaluator}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class OwnershipEvaluatorTest {

    private static final Long CURRENT_USER_ID = 10L;

    @Mock
    private CommentService commentService;

    @Mock
    private EntryService entryService;

    @InjectMocks
    private OwnershipEvaluator ownershipEvaluator;

    private Authentication authentication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(method = "isSelf", source = OwnershipEvaluatorParameterProvider.class)
    public void shouldValidateSelf(Long userID, boolean expectedResult) {

        // given
        prepareAuthenticationObject(Role.USER);

        // when
        boolean result = ownershipEvaluator.isSelf(authentication, userID);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    @Parameters(method = "isSelfOrAdmin", source = OwnershipEvaluatorParameterProvider.class)
    public void shouldValidateSelfOrAdmin(Long userID, Role role, boolean expectedResult) {

        // given
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isSelfOrAdmin(authentication, userID);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    @Parameters(method = "isOwnEntryOrAdmin", source = OwnershipEvaluatorParameterProvider.class)
    public void shouldValidateEntry(Role role, Long ownerID, boolean expectedResult) throws ServiceException {

        // given
        EntryVO entryVO = EntryVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(ownerID)
                        .build())
                .build();
        given(entryService.getOne(anyLong())).willReturn(entryVO);
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isOwnEntryOrAdmin(authentication, 1L);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldEntryValidationReturnFalseOnRetrievalFailure() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(entryService).getOne(anyLong());
        prepareAuthenticationObject(Role.USER);

        // when
        boolean result = ownershipEvaluator.isOwnEntryOrAdmin(authentication, 1L);

        // then
        assertThat(result, is(false));
    }

    @Test
    @Parameters(method = "isOwnCommentOrModerator", source = OwnershipEvaluatorParameterProvider.class)
    public void shouldValidateComment(Role role, Long ownerID, boolean expectedResult) throws ServiceException {

        // given
        CommentVO commentVO = CommentVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(ownerID)
                        .build())
                .build();
        given(commentService.getOne(anyLong())).willReturn(commentVO);
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isOwnCommentOrModerator(authentication, 1L);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    @Parameters(method = "isOwnCommentOrModerator", source = OwnershipEvaluatorParameterProvider.class)
    public void shouldValidateCommentByEntity(Role role, Long ownerID, boolean expectedResult) throws ServiceException {

        // given
        CommentVO commentVO = CommentVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(ownerID)
                        .build())
                .build();
        given(commentService.getOne(anyLong())).willReturn(commentVO);
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isOwnCommentOrModerator(authentication, CommentVO.wrapMinimumVO(1L));

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldCommentValidationReturnFalseOnRetrievalFailure() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(commentService).getOne(anyLong());
        prepareAuthenticationObject(Role.USER);

        // when
        boolean result = ownershipEvaluator.isOwnCommentOrModerator(authentication, CommentVO.wrapMinimumVO(1L));

        // then
        assertThat(result, is(false));
    }

    private void prepareAuthenticationObject(Role role) {

        authentication = JWTAuthenticationToken.getBuilder()
                .withPayload(JWTPayload.getBuilder()
                        .withId(CURRENT_USER_ID.intValue())
                        .withRole(role)
                        .build())
                .build();
    }

    public static class OwnershipEvaluatorParameterProvider {

        public static Object[] isSelf() {
            return new Object[] {
                    new Object[] {CURRENT_USER_ID, true},
                    new Object[] {2L, false}
            };
        }

        public static Object[] isSelfOrAdmin() {
            return new Object[] {
                    new Object[] {CURRENT_USER_ID, Role.USER, true},
                    new Object[] {2L, Role.ADMIN, true},
                    new Object[] {2L, Role.USER, false}
            };
        }

        public static Object[] isOwnEntryOrAdmin() {
            return new Object[] {
                    new Object[] {Role.ADMIN, 2L, true},
                    new Object[] {Role.ADMIN, CURRENT_USER_ID, true},
                    new Object[] {Role.EDITOR, CURRENT_USER_ID, true},
                    new Object[] {Role.EDITOR, 2L, false}
            };
        }

        public static Object[] isOwnCommentOrModerator() {
            return new Object[] {
                    new Object[] {Role.ADMIN, 2L, true},
                    new Object[] {Role.ADMIN, CURRENT_USER_ID, true},
                    new Object[] {Role.EDITOR, 2L, true},
                    new Object[] {Role.EDITOR, CURRENT_USER_ID, true},
                    new Object[] {Role.USER, 2L, false},
                    new Object[] {Role.USER, CURRENT_USER_ID, true},
            };
        }
    }
}
