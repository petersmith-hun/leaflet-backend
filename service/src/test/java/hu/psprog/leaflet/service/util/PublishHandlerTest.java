package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link PublishHandler}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class PublishHandlerTest {

    private static final Long ENTRY_ID = 1L;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String NULL_STRING = "null";

    private static final Date PUBLISH_DATE = new Date();
    private static final Date ALREADY_PUBLIC_PUBLISH_DATE = new Calendar.Builder().setDate(2019, 4, 1).build().getTime();
    private static final String PUBLISH_DATE_AS_STRING = SIMPLE_DATE_FORMAT.format(PUBLISH_DATE);
    private static final String ALREADY_PUBLIC_PUBLISH_DATE_AS_STRING = SIMPLE_DATE_FORMAT.format(ALREADY_PUBLIC_PUBLISH_DATE);

    @Mock
    private EntryDAO entryDAO;

    @InjectMocks
    private PublishHandler publishHandler;

    @ParameterizedTest
    @MethodSource("forCreation")
    public void shouldUpdatePublishDateSetPublishDateUponCreation(Entry entryToBeUpdated, boolean expectPublishDateSet) {

        // when
        publishHandler.updatePublishDate(entryToBeUpdated);

        // then
        if (expectPublishDateSet) {
            assertThat(SIMPLE_DATE_FORMAT.format(entryToBeUpdated.getPublished()), equalTo(PUBLISH_DATE_AS_STRING));
        } else {
            assertThat(entryToBeUpdated.getPublished(), nullValue());
        }
    }

    @ParameterizedTest
    @MethodSource("forUpdate")
    public void shouldUpdatePublishDateSetPublishDateUponUpdate(Entry storedEntry, Entry entryToBeUpdated, String expectedPublishDate) {

        // given
        given(entryDAO.findOne(ENTRY_ID)).willReturn(storedEntry);

        // when
        publishHandler.updatePublishDate(ENTRY_ID, entryToBeUpdated);

        // then
        if (NULL_STRING.equals(expectedPublishDate)) {
            assertThat(entryToBeUpdated.getPublished(), nullValue());
        } else {
            assertThat(SIMPLE_DATE_FORMAT.format(entryToBeUpdated.getPublished()), equalTo(expectedPublishDate));
        }
    }

    @Test
    public void shouldUpdatePublishDateDoesNothingForNonExistingEntry() {

        // given
        Entry entry = new Entry();

        // when
        publishHandler.updatePublishDate(entry);

        // then
        assertThat(entry.getPublished(), nullValue());
    }

    private static Stream<Arguments> forCreation() {

        return Stream.of(
                Arguments.of(prepareEntry(false), false),
                Arguments.of(prepareEntry(true), true)
        );
    }

    private static Stream<Arguments> forUpdate() {

        return Stream.of(
                Arguments.of(prepareEntry(false), prepareEntry(false), NULL_STRING),
                Arguments.of(prepareEntry(false), prepareEntry(true), PUBLISH_DATE_AS_STRING),
                Arguments.of(prepareAlreadyPublishedEntry(), prepareEntry(false), ALREADY_PUBLIC_PUBLISH_DATE_AS_STRING),
                Arguments.of(prepareAlreadyPublishedEntry(), prepareEntry(true), ALREADY_PUBLIC_PUBLISH_DATE_AS_STRING)
        );
    }

    private static Entry prepareEntry(boolean published) {

        Entry entry = new Entry();
        entry.setStatus(published
                ? EntryStatus.PUBLIC
                : EntryStatus.DRAFT);

        return entry;
    }

    private static Entry prepareAlreadyPublishedEntry() {

        Entry entry = prepareEntry(true);
        entry.setPublished(ALREADY_PUBLIC_PUBLISH_DATE);

        return entry;
    }
}
