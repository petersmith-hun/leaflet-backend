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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.stream.Stream;

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
@ExtendWith(MockitoExtension.class)
public class OwnershipEvaluatorTest {

    private static final Long CURRENT_USER_ID = 10L;

    @Mock(lenient = true)
    private CommentService commentService;

    @Mock(lenient = true)
    private EntryService entryService;

    @InjectMocks
    private OwnershipEvaluator ownershipEvaluator;

    private Authentication authentication;

    @ParameterizedTest
    @MethodSource("isSelf")
    public void shouldValidateSelf(Long userID, boolean expectedResult) {

        // given
        prepareAuthenticationObject(Role.USER);

        // when
        boolean result = ownershipEvaluator.isSelf(authentication, userID);

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("isSelfOrAdmin")
    public void shouldValidateSelfOrAdmin(Long userID, Role role, boolean expectedResult) {

        // given
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isSelfOrAdmin(authentication, userID);

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("isSelfOrModerator")
    public void shouldValidateSelfOrModerator(Long userID, Role role, boolean expectedResult) {

        // given
        prepareAuthenticationObject(role);

        // when
        boolean result = ownershipEvaluator.isSelfOrModerator(authentication, UserVO.wrapMinimumVO(userID));

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("isOwnEntryOrAdmin")
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

    @ParameterizedTest
    @MethodSource("isOwnCommentOrModerator")
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

    @ParameterizedTest
    @MethodSource("isOwnCommentOrModerator")
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

    private static Stream<Arguments> isSelf() {

        return Stream.of(
                Arguments.of(CURRENT_USER_ID, true),
                Arguments.of(2L, false)
        );
    }

    private static Stream<Arguments> isSelfOrAdmin() {

        return Stream.of(
                Arguments.of(CURRENT_USER_ID, Role.USER, true),
                Arguments.of(2L, Role.ADMIN, true),
                Arguments.of(2L, Role.USER, false)
        );
    }

    private static Stream<Arguments> isSelfOrModerator() {

        return Stream.of(
                Arguments.of(CURRENT_USER_ID, Role.USER, true),
                Arguments.of(2L, Role.ADMIN, true),
                Arguments.of(2L, Role.EDITOR, true),
                Arguments.of(2L, Role.USER, false)
        );
    }

    private static Stream<Arguments> isOwnEntryOrAdmin() {

        return Stream.of(
                Arguments.of(Role.ADMIN, 2L, true),
                Arguments.of(Role.ADMIN, CURRENT_USER_ID, true),
                Arguments.of(Role.EDITOR, CURRENT_USER_ID, true),
                Arguments.of(Role.EDITOR, 2L, false)
        );
    }

    private static Stream<Arguments> isOwnCommentOrModerator() {

        return Stream.of(
                Arguments.of(Role.ADMIN, 2L, true),
                Arguments.of(Role.ADMIN, CURRENT_USER_ID, true),
                Arguments.of(Role.EDITOR, 2L, true),
                Arguments.of(Role.EDITOR, CURRENT_USER_ID, true),
                Arguments.of(Role.USER, 2L, false),
                Arguments.of(Role.USER, CURRENT_USER_ID, true)
        );
    }
}
