package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;
import java.util.Comparator;
import java.util.stream.Collectors;

import static hu.psprog.leaflet.bridge.client.domain.OrderBy.Entry.CREATED;
import static hu.psprog.leaflet.bridge.client.domain.OrderBy.Entry.TITLE;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.ASC;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.DESC;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /entries} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class EntriesControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_ENTRIES = 25;
    private static final Comparator<EntryDataModel> ASC_BY_CREATED = Comparator.comparing(EntryDataModel::getCreated);
    private static final Comparator<EntryDataModel> DESC_BY_CREATED = Comparator.comparing(EntryDataModel::getCreated).reversed();
    private static final Comparator<EntryDataModel> ASC_BY_TITLE = Comparator.comparing(EntryDataModel::getTitle);
    private static final Comparator<EntryDataModel> DESC_BY_TITLE = Comparator.comparing(EntryDataModel::getTitle).reversed();

    private static final String CONTROL_ENTRY_LINK = "entry-1";
    private static final Long CONTROL_ENTRY_ID = 1L;
    private static final String CONTROL_ENTRY_26 = "entry-26";
    private static final GenericType<WrapperBodyDataModel<ExtendedEntryDataModel>> GENERIC_TYPE_EXTENDED_ENTRY_DATA_MODEL = new GenericType<WrapperBodyDataModel<ExtendedEntryDataModel>>() {};
    private static final GenericType<WrapperBodyDataModel<EditEntryDataModel>> GENERIC_TYPE_EDIT_ENTRY_DATA_MODEL = new GenericType<WrapperBodyDataModel<EditEntryDataModel>>() {};

    @Autowired
    private EntryBridgeService entryBridgeService;

    @Test
    public void shouldReturnAllEntries() throws CommunicationFailureException {

        // when
        EntryListDataModel result = entryBridgeService.getAllEntries();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getEntries().size(), equalTo(NUMBER_OF_ALL_ENTRIES));
    }

    @Test
    @Parameters(source = EntryAcceptanceTestDataProvider.class, method = "pageOfEntries")
    public void shouldReturnPageOfEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                          long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfEntries(page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
    }

    @Test
    @Parameters(source = EntryAcceptanceTestDataProvider.class, method = "pageOfPublicEntries")
    public void shouldReturnPageOfPublicEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntries(page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
    }

    @Test
    @Parameters(source = EntryAcceptanceTestDataProvider.class, method = "pageOfPublicEntriesByCategory")
    public void shouldReturnPageOfPublicEntriesByCategory(Long categoryID, int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                          long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByCategory(categoryID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
    }

    @Test
    public void shouldReturnExistingEntryByLink() throws CommunicationFailureException {

        // given
        WrapperBodyDataModel<ExtendedEntryDataModel> control = getControl(CONTROL_ENTRY_LINK, GENERIC_TYPE_EXTENDED_ENTRY_DATA_MODEL);

        // when
        WrapperBodyDataModel<ExtendedEntryDataModel> result = entryBridgeService.getEntryByLink(CONTROL_ENTRY_LINK);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    public void shouldReturnExistingEntryByID() throws CommunicationFailureException {

        // given
        WrapperBodyDataModel<EditEntryDataModel> control = getControl(CONTROL_ENTRY_LINK, CONTROL_SUFFIX_EDIT, GENERIC_TYPE_EDIT_ENTRY_DATA_MODEL);

        // when
        WrapperBodyDataModel<EditEntryDataModel> result = entryBridgeService.getEntryByID(CONTROL_ENTRY_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldEntryByLinkReturnHTTP404ForNonExistingEntry() throws CommunicationFailureException {

        // given
        String link = "entry-non-existing-1";

        // when
        entryBridgeService.getEntryByLink(link);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldCreateEntry() throws CommunicationFailureException {

        // given
        EntryCreateRequestModel entryCreateRequestModel = getControl(CONTROL_ENTRY_26, CONTROL_SUFFIX_CREATE, EntryCreateRequestModel.class);

        // when
        entryBridgeService.createEntry(entryCreateRequestModel);

        // then
        assertModifiedEntry(26L, entryCreateRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldUpdateEntry() throws CommunicationFailureException {

        // given
        EntryUpdateRequestModel entryUpdateRequestModel = getControl(CONTROL_ENTRY_LINK, CONTROL_SUFFIX_MODIFY, EntryUpdateRequestModel.class);

        // when
        entryBridgeService.updateEntry(CONTROL_ENTRY_ID, entryUpdateRequestModel);

        // then
        assertModifiedEntry(CONTROL_ENTRY_ID, entryUpdateRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldChangeStatus() throws CommunicationFailureException {

        // when
        entryBridgeService.changeStatus(CONTROL_ENTRY_ID);

        // then
        assertThat(entryBridgeService.getEntryByID(CONTROL_ENTRY_ID).getBody().isEnabled(), is(false));
        assertThat(entryBridgeService.getPageOfPublicEntries(1, 30, CREATED, ASC).getBody().getEntries().stream()
                .noneMatch(entryDataModel -> CONTROL_ENTRY_LINK.equals(entryDataModel.getLink())), is(true));
    }

    @Test(expected = ResourceNotFoundException.class)
    @ResetDatabase
    public void shouldDeleteEntry() throws CommunicationFailureException {

        // given
        Long entryToDelete = 25L;

        // when
        entryBridgeService.deleteEntry(entryToDelete);

        // then
        // this call should result in exception
        entryBridgeService.getEntryByID(entryToDelete);
    }
    
    private void assertModifiedEntry(Long entryID, EntryUpdateRequestModel expected) throws CommunicationFailureException {
        assertThat(entryBridgeService.getEntryByLink(expected.getLink()).getBody().getContent(), equalTo(expected.getContent()));
        WrapperBodyDataModel<EditEntryDataModel> current = entryBridgeService.getEntryByID(entryID);
        assertThat(current.getBody().getCategory().getId(), equalTo(expected.getCategoryID()));
        assertThat(current.getBody().isEnabled(), is(expected.isEnabled()));
        assertThat(current.getBody().getLink(), equalTo(expected.getLink()));
        assertThat(current.getBody().getLocale().toLowerCase(), equalTo(expected.getLocale().getLanguage()));
        assertThat(current.getBody().getPrologue(), equalTo(expected.getPrologue()));
        assertThat(current.getBody().getRawContent(), equalTo(expected.getRawContent()));
        assertThat(current.getBody().getEntryStatus(), equalTo(expected.getStatus().name()));
        assertThat(current.getBody().getTitle(), equalTo(expected.getTitle()));
        assertThat(current.getSeo().getMetaDescription(), equalTo(expected.getMetaDescription()));
        assertThat(current.getSeo().getMetaKeywords(), equalTo(expected.getMetaKeywords()));
        assertThat(current.getSeo().getMetaTitle(), equalTo(expected.getMetaTitle()));

        if (expected instanceof EntryCreateRequestModel) {
            assertThat(current.getBody().getUser().getId(), equalTo(((EntryCreateRequestModel) expected).getUserID()));
        }
    }

    private void assertPaginatedResult(WrapperBodyDataModel<EntryListDataModel> result, Comparator<EntryDataModel> comparator,
                                       long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {

        assertThat(result, notNullValue());
        assertThat(result.getBody().getEntries().size(), equalTo(expectedBodySize));
        assertThat(result.getPagination(), notNullValue());
        assertThat(result.getPagination().getPageCount(), equalTo(expectedPageCount));
        assertThat(result.getPagination().isHasNext(), equalTo(expectedHasNext));
        assertThat(result.getPagination().isHasPrevious(), equalTo(expectedHasPrevious));
        assertThat(result.getPagination().getEntityCount(), equalTo(expectedEntityCount));
        assertThat(result.getBody().getEntries().stream()
                .sorted(comparator)
                .collect(Collectors.toList())
                .equals(result.getBody().getEntries()), is(true));
    }

    private Comparator<EntryDataModel> getComparator(OrderBy.Entry orderBy, OrderDirection orderDirection) {

        if (orderBy == CREATED && orderDirection == ASC) {
            return ASC_BY_CREATED;
        } else if (orderBy == CREATED && orderDirection == DESC) {
            return DESC_BY_CREATED;
        } else if (orderBy == TITLE && orderDirection == ASC) {
            return ASC_BY_TITLE;
        } else {
            return DESC_BY_TITLE;
        }
    }

    public static class EntryAcceptanceTestDataProvider {

        public static Object[] pageOfEntries() {
            return new Object[] {
                    // page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                    new Object[] {1, 10, CREATED, ASC,  25, 10, 3, true,  false},
                    new Object[] {2, 10, CREATED, ASC,  25, 10, 3, true,  true},
                    new Object[] {3, 10, CREATED, ASC,  25, 5,  3, false, true},
                    new Object[] {1, 30, CREATED, ASC,  25, 25, 1, false, false},
                    new Object[] {1, 30, TITLE,   DESC, 25, 25, 1, false, false},
                    new Object[] {3, 8,  TITLE,   ASC,  25, 8,  4, true,  true},
                    new Object[] {2, 8,  CREATED, DESC, 25, 8,  4, true,  true},
            };
        }

        public static Object[] pageOfPublicEntries() {
            return new Object[] {
                    // page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                    new Object[] {1, 10, CREATED, ASC,  19, 10, 2, true,  false},
                    new Object[] {2, 10, CREATED, ASC,  19, 9,  2, false, true},
                    new Object[] {3, 10, CREATED, ASC,  19, 0,  2, false, true},
                    new Object[] {1, 30, CREATED, ASC,  19, 19, 1, false, false},
                    new Object[] {1, 30, TITLE,   DESC, 19, 19, 1, false, false},
                    new Object[] {2, 8,  TITLE,   ASC,  19, 8,  3, true,  true},
                    new Object[] {3, 8,  CREATED, DESC, 19, 3,  3, false,  true},
            };
        }

        public static Object[] pageOfPublicEntriesByCategory() {
            return new Object[] {
                    // category ID, page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                    new Object[] {1L, 1, 5,  CREATED, ASC,  8,  5,  2, true,  false},
                    new Object[] {1L, 2, 5,  CREATED, ASC,  8,  3,  2, false, true},
                    new Object[] {2L, 1, 10, CREATED, ASC,  11, 10, 2, true,  false},
                    new Object[] {2L, 2, 10, CREATED, ASC,  11, 1,  2, false, true},
                    new Object[] {1L, 2, 10, CREATED, ASC,  8,  0,  1, false, true},
                    new Object[] {2L, 1, 30, CREATED, ASC,  11, 11, 1, false, false},
                    new Object[] {1L, 1, 30, TITLE,   DESC, 8,  8,  1, false, false},
                    new Object[] {1L, 2, 3,  TITLE,   ASC,  8,  3,  3, true,  true}
            };
        }
    }
}
