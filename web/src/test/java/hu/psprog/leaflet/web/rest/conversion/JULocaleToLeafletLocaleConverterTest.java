package hu.psprog.leaflet.web.rest.conversion;

import hu.psprog.leaflet.persistence.entity.Locale;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link JULocaleToLeafletLocaleConverter}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class JULocaleToLeafletLocaleConverterTest {

    @InjectMocks
    private JULocaleToLeafletLocaleConverter converter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(source = LocaleConverterParameterProvider.class)
    public void shouldConvert(String localeCode, Locale expectedLocale) {

        // given
        java.util.Locale locale = java.util.Locale.forLanguageTag(localeCode);

        // when
        Locale result = converter.convert(locale);

        // then
        assertThat(result, equalTo(expectedLocale));
    }

    public static class LocaleConverterParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {"hu", Locale.HU},
                    new Object[] {"en", Locale.EN},
                    new Object[] {"unknown", Locale.EN}
            };
        }
    }
}