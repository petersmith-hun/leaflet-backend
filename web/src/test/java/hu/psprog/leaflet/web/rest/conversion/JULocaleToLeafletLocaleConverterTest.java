package hu.psprog.leaflet.web.rest.conversion;

import hu.psprog.leaflet.persistence.entity.Locale;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link JULocaleToLeafletLocaleConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class JULocaleToLeafletLocaleConverterTest {

    @InjectMocks
    private JULocaleToLeafletLocaleConverter converter;

    @ParameterizedTest
    @MethodSource("localConverterDataProvider")
    public void shouldConvert(String localeCode, Locale expectedLocale) {

        // given
        java.util.Locale locale = java.util.Locale.forLanguageTag(localeCode);

        // when
        Locale result = converter.convert(locale);

        // then
        assertThat(result, equalTo(expectedLocale));
    }

    private static Stream<Arguments> localConverterDataProvider() {

        return Stream.of(
                Arguments.of("hu", Locale.HU),
                Arguments.of("en", Locale.EN),
                Arguments.of("unknown", Locale.EN)
        );
    }
}