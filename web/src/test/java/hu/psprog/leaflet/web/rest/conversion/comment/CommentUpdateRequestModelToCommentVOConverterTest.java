package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommentUpdateRequestModelToCommentVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentUpdateRequestModelToCommentVOConverterTest extends ConversionTestObjects {

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

        // when
        CommentVO result = converter.convert(COMMENT_CREATE_REQUEST_MODEL);

        // then
        assertThat(result, equalTo(COMMENT_VO_FOR_CREATE));
    }
}