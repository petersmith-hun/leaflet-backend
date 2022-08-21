package hu.psprog.leaflet.service.security.evaluator;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link OwnershipEvaluator}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class OwnershipEvaluatorTest {

    private static final Long CURRENT_USER_ID = 10L;
    private static final Long ENTRY_ID  = 20L;
    private static final Long COMMENT_ID  = 30L;

    @Mock
    private CommentDAO commentDAO;

    @Mock
    private EntryDAO entryDAO;

    @InjectMocks
    private OwnershipEvaluator ownershipEvaluator;

    private Authentication authentication;

    @BeforeEach
    public void setup() {
        prepareAuthenticationObject();
    }

    @ParameterizedTest
    @MethodSource("userIDDataProvider")
    public void shouldValidateSelf(Long userID, boolean expectedResult) {

        // when
        boolean result = ownershipEvaluator.isSelf(authentication, userID);

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("userIDDataProvider")
    public void shouldValidateSelfByEntity(Long userID, boolean expectedResult) {

        // when
        boolean result = ownershipEvaluator.isSelf(authentication, UserVO.wrapMinimumVO(userID));

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("userIDDataProvider")
    public void shouldValidateOwnEntry(Long userID, boolean expectedResult) {

        // given
        Entry entry = prepareEntry(userID);
        given(entryDAO.findById(ENTRY_ID)).willReturn(Optional.of(entry));

        // when
        boolean result = ownershipEvaluator.isOwnEntry(authentication, ENTRY_ID);

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("userIDDataProvider")
    public void shouldValidateOwnComment(Long userID, boolean expectedResult) {

        // given
        Comment comment = prepareComment(userID);
        given(commentDAO.findById(COMMENT_ID)).willReturn(Optional.of(comment));

        // when
        boolean result = ownershipEvaluator.isOwnComment(authentication, COMMENT_ID);

        // then
        assertThat(result, is(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("userIDDataProvider")
    public void shouldValidateOwnCommentByEntity(Long userID, boolean expectedResult) {

        // given
        Comment comment = prepareComment(userID);
        CommentVO commentVO = CommentVO.wrapMinimumVO(COMMENT_ID);
        given(commentDAO.findById(COMMENT_ID)).willReturn(Optional.of(comment));

        // when
        boolean result = ownershipEvaluator.isOwnComment(authentication, commentVO);

        // then
        assertThat(result, is(expectedResult));
    }

    private void prepareAuthenticationObject() {

        authentication = new JwtAuthenticationToken(Jwt.withTokenValue("token1")
                .claim("uid", CURRENT_USER_ID)
                .header("typ", "JWT")
                .header("alg", "HS256")
                .build());
    }

    private Entry prepareEntry(Long userID) {

        return Entry.getBuilder()
                .withId(ENTRY_ID)
                .withUser(User.getBuilder()
                        .withId(userID)
                        .build())
                .build();
    }

    private Comment prepareComment(Long userID) {

        return Comment.getBuilder()
                .withId(COMMENT_ID)
                .withUser(User.getBuilder()
                        .withId(userID)
                        .build())
                .build();
    }

    private static Stream<Arguments> userIDDataProvider() {

        return Stream.of(
                Arguments.of(CURRENT_USER_ID, true),
                Arguments.of(2L, false)
        );
    }
}
