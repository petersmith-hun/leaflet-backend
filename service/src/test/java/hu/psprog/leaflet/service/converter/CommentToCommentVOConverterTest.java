package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.CommentVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CommentToCommentVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommentToCommentVOConverterTest extends ConversionTestObjects {

    @Mock
    private UserToUserVOConverter userToUserVOConverter;

    @Mock
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @InjectMocks
    private CommentToCommentVOConverter converter;

    @Test
    public void shouldConvertNonDeletedComment() {

        // given
        given(userToUserVOConverter.convert(USER_MINIMUM)).willReturn(USER_VO);
        given(entryToEntryVOConverter.convert(ENTRY_MINIMUM)).willReturn(ENTRY_VO);

        // when
        CommentVO result = converter.convert(COMMENT);

        // then
        assertThat(result, equalTo(COMMENT_VO));
    }

    @Test
    public void shouldConvertDeletedComment() {

        // given
        given(userToUserVOConverter.convert(USER_MINIMUM)).willReturn(USER_VO);
        given(entryToEntryVOConverter.convert(ENTRY_MINIMUM)).willReturn(ENTRY_VO);

        // when
        CommentVO result = converter.convert(COMMENT_DELETED);

        // then
        assertThat(result, equalTo(COMMENT_VO_DELETED));
    }

    @Test
    public void shouldConvertMinimum() {

        // when
        CommentVO result = converter.convert(COMMENT_MINIMUM);

        // then
        assertThat(result, equalTo(COMMENT_MINIMUM_VO));
    }
}