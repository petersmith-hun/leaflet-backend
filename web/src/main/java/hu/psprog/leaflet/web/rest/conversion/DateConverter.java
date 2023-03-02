package hu.psprog.leaflet.web.rest.conversion;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Date converter utility.
 *
 * @author Peter Smith
 */
@Component
public class DateConverter {

    /**
     * Converts the given {@link Date} to {@link ZonedDateTime}.
     *
     * @param date date of type {@link Date} to be converter
     * @return converted {@link ZonedDateTime}
     */
    public ZonedDateTime convert(Date date) {

        return Optional.ofNullable(date)
                .map(originalDate -> originalDate.toInstant().atZone(ZoneId.systemDefault()))
                .orElse(null);
    }
}
