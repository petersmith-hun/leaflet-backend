package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommentVOToCommentDataModelConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentVOToCommentDataModelConverterTest extends ConversionTestObjects {

    @InjectMocks
    private CommentVOToCommentDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // when
        CommentDataModel result = converter.convert(COMMENT_VO);

        // then
        assertThat(result, equalTo(COMMENT_DATA_MODEL));
    }
}