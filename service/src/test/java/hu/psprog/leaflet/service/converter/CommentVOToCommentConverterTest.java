package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommentVOToCommentConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentVOToCommentConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CommentVOToCommentConverter converter;

    @Test
    public void shouldConvertFullVO() {

        // when
        Comment result = converter.convert(COMMENT_VO);

        // then
        assertThat(result, equalTo(COMMENT));
    }

    @Test
    public void shouldConvertMinimumVO() {

        // when
        Comment result = converter.convert(COMMENT_MINIMUM_VO);

        // then
        assertThat(result, equalTo(COMMENT_MINIMUM));
    }
}