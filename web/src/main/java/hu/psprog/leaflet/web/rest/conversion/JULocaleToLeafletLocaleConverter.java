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

    @Override
    public Locale convert(java.util.Locale locale) {

        switch (locale.getLanguage()) {
            case "hu":
            case "hu_HU":
                return Locale.HU;
            default:
                return Locale.EN;
        }
    }
}
