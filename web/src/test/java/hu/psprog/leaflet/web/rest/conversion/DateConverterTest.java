package hu.psprog.leaflet.web.rest.conversion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DateConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DateConverterTest {

    private static final Date SOURCE_DATE = new Calendar.Builder()
            .set(Calendar.YEAR, 2019)
            .set(Calendar.MONTH, 1)
            .set(Calendar.DAY_OF_MONTH, 9)
            .set(Calendar.HOUR_OF_DAY, 14)
            .set(Calendar.MINUTE, 30)
            .set(Calendar.SECOND, 45)
            .set(Calendar.MILLISECOND, 0)
            .setTimeZone(TimeZone.getDefault())
            .build()
            .getTime();

    private static final ZonedDateTime EXPECTED_ZONED_DATE_TIME = ZonedDateTime.of(2019, 2, 9, 14, 30, 45, 0, ZoneId.systemDefault());

    @InjectMocks
    private DateConverter dateConverter;

    @Test
    public void shouldConvertDate() {

        // when
        ZonedDateTime result = dateConverter.convert(SOURCE_DATE);

        // then
        assertThat(result, equalTo(EXPECTED_ZONED_DATE_TIME));
    }

    @Test
    public void shouldConvertHandleNull() {

        // when
        ZonedDateTime result = dateConverter.convert(null);

        // then
        assertThat(result, nullValue());
    }
}
