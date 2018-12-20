package hu.psprog.leaflet.web.rest.conversion;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CommonFormatter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonFormatterTest {

    private static final Date DATE_TO_FORMAT = prepareDate();
    private static final String FORMATTED_DATE = "Thursday, January 18, 2018, 8:28 PM";

    @InjectMocks
    private CommonFormatter commonFormatter;

    @Test
    public void shouldFormatDate() {

        // when
        String result = commonFormatter.formatDate(DATE_TO_FORMAT, Locale.ENGLISH);

        // then
        assertThat(result, equalTo(FORMATTED_DATE));
    }

    @Test
    public void shouldReturnEmptyStringForNullDate() {

        // when
        String result = commonFormatter.formatDate(null, Locale.ENGLISH);

        // then
        assertThat(result, equalTo(StringUtils.EMPTY));
    }

    private static Date prepareDate() {
        return new Calendar.Builder()
                .setDate(2018, 0, 18)
                .setTimeOfDay(20, 28, 35)
                .build()
                .getTime();
    }
}