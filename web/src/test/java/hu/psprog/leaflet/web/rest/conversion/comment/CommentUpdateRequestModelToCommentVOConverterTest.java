package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentUpdateRequestModelToCommentVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentUpdateRequestModelToCommentVOConverterTest extends ConversionTestObjects {

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @InjectMocks
    private CommentUpdateRequestModelToCommentVOConverter converter;

    @Test
    public void shouldConvertUpdateRequest() {

        // when
        CommentVO result = converter.convert(COMMENT_UPDATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(COMMENT_VO_FOR_UPDATE));
    }

    @Test
    public void shouldConvertCreateRequest() {

        // given
        given(jwtAuthenticationToken.getTokenAttributes()).willReturn(Map.of(
                "uid", 1L
        ));
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);

        // when
        CommentVO result = converter.convert(COMMENT_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(COMMENT_VO_FOR_CREATE));
    }

    @Test
    public void shouldConvertThrowExceptionWhenUserIsNotAuthenticated() {

        // given
        SecurityContextHolder.clearContext();

        // when
        assertThrows(AccessDeniedException.class, () -> converter.convert(COMMENT_CREATE_REQUEST_MODEL));

        // then
        // exception expected
    }
}