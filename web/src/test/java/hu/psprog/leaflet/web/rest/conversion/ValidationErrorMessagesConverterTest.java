package hu.psprog.leaflet.web.rest.conversion;

import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ValidationErrorMessageListDataModel}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ValidationErrorMessagesConverterTest extends ConversionTestObjects {

    @InjectMocks
    private ValidationErrorMessagesConverter converter;

    @Test
    public void shouldConvert() {

        // when
        ValidationErrorMessageListDataModel result = converter.convert(OBJECT_ERROR_LIST);

        // then
        assertThat(result, equalTo(VALIDATION_ERROR_MESSAGE_LIST_DATA_MODEL));
    }
}