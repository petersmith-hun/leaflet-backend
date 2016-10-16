package hu.psprog.leaflet.web.rest.conversion;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Formatter tools.
 *
 * @author Peter Smith
 */
@Component
public class CommonFormatter {

    private static final String EMPTY_STRING = "";
    private static final int DATE_STYLE = DateFormat.FULL;
    private static final int TIME_STYLE = DateFormat.SHORT;

    /**
     * Formats given date for the given locale and returns as a string.
     *
     * @param date {@link Date} to format
     * @param locale {@link Locale} to format date for
     * @return formatted date
     */
    public String formatDate(Date date, Locale locale) {

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DATE_STYLE, TIME_STYLE, locale);

        return date == null ? EMPTY_STRING : dateFormat.format(date);
    }
}
