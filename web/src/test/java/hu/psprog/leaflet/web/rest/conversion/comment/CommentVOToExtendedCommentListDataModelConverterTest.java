package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentVOToExtendedCommentListDataModelConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentVOToExtendedCommentListDataModelConverterTest extends ConversionTestObjects {

    @Mock
    private CommentVOToExtendedCommentDataModelConverter entityConverter;

    @InjectMocks
    private CommentVOToExtendedCommentListDataModelConverter converter;

    @Test
    public void shouldConvert() {

        // given
        given(entityConverter.convert(COMMENT_VO)).willReturn(EXTENDED_COMMENT_DATA_MODEL);

        // when
        ExtendedCommentListDataModel result = converter.convert(Collections.singletonList(COMMENT_VO));

        // then
        assertThat(result, equalTo(EXTENDED_COMMENT_LIST_DATA_MODEL));
    }
}