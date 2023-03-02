package hu.psprog.leaflet.web.rest.conversion;

import org.springframework.core.convert.converter.Converter;

import hu.psprog.leaflet.persistence.entity.Locale;
import org.springframework.stereotype.Component;

/**
 * Converts {@link java.util.Locale} to {@link Locale}.
 *
 * @author Peter Smith
 */
@Component
public class JULocaleToLeafletLocaleConverter implements Converter<java.util.Locale, Locale> {

    private static final String HUNGARIAN_LANGUAGE_CODE = "hu";

    @Override
    public Locale convert(java.util.Locale locale) {

        return HUNGARIAN_LANGUAGE_CODE.equals(locale.getLanguage())
                ? Locale.HU
                : Locale.EN;
    }
}
