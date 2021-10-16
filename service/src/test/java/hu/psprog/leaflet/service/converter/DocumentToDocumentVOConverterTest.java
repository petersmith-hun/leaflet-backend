package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.service.vo.DocumentVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link DocumentToDocumentVOConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DocumentToDocumentVOConverterTest extends ConversionTestObjects {

    @Mock
    private UserToUserVOConverter userToUserVOConverter;

    @InjectMocks
    private DocumentToDocumentVOConverter converter;

    @Test
    public void shouldConvertWithOwner() {

        // given
        given(userToUserVOConverter.convert(USER)).willReturn(USER_VO);

        // when
        DocumentVO result = converter.convert(DOCUMENT_WITH_OWNER);

        // then
        assertThat(result, equalTo(DOCUMENT_VO_WITH_OWNER));
    }

    @Test
    public void shouldConvertWithoutOwner() {

        // when
        DocumentVO result = converter.convert(DOCUMENT_WITHOUT_OWNER);

        // then
        assertThat(result, equalTo(DOCUMENT_VO_WITHOUT_OWNER));
    }

    @Test
    public void shouldConvertMinimum() {

        // when
        DocumentVO result = converter.convert(DOCUMENT_MINIMUM);

        // then
        assertThat(result, equalTo(DOCUMENT_MINIMUM_VO));
    }
}