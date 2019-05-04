package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link PublishHandler}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(source = PublishHandlerDataProvider.class, method = "forCreation")
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

    @Test
    @Parameters(source = PublishHandlerDataProvider.class, method = "forUpdate")
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
        given(entryDAO.findOne(ENTRY_ID)).willReturn(null);

        // when
        publishHandler.updatePublishDate(entry);

        // then
        assertThat(entry.getPublished(), nullValue());
    }

    public static class PublishHandlerDataProvider {

        public static Object[] forCreation() {

            return new Object[] {
                    new Object[] {prepareEntry(false), false},
                    new Object[] {prepareEntry(true), true}
            };
        }

        public static Object[] forUpdate() {

            return new Object[] {
                    new Object[] {prepareEntry(false), prepareEntry(false), NULL_STRING},
                    new Object[] {prepareEntry(false), prepareEntry(true), PUBLISH_DATE_AS_STRING},
                    new Object[] {prepareAlreadyPublishedEntry(), prepareEntry(false), ALREADY_PUBLIC_PUBLISH_DATE_AS_STRING},
                    new Object[] {prepareAlreadyPublishedEntry(), prepareEntry(true), ALREADY_PUBLIC_PUBLISH_DATE_AS_STRING}
            };
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
}
