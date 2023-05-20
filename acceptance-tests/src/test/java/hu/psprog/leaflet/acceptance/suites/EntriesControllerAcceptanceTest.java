package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.request.entry.EntrySearchParameters;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.MenuDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntrySearchResultDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ConflictingRequestException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.stream.Stream;

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
@LeafletAcceptanceSuite
public class EntriesControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_ENTRIES = 25;
    private static final Comparator<EntryDataModel> ASC_BY_CREATED = Comparator.comparing(EntryDataModel::created);
    private static final Comparator<EntryDataModel> DESC_BY_CREATED = Comparator.comparing(EntryDataModel::created).reversed();
    private static final Comparator<EntryDataModel> ASC_BY_TITLE = Comparator.comparing(EntryDataModel::title);
    private static final Comparator<EntryDataModel> DESC_BY_TITLE = Comparator.comparing(EntryDataModel::title).reversed();

    private static final String CONTROL_ENTRY_LINK = "entry-1";
    private static final Long CONTROL_ENTRY_ID = 1L;
    private static final String CONTROL_ENTRY_26 = "entry-26";
    private static final GenericType<WrapperBodyDataModel<ExtendedEntryDataModel>> GENERIC_TYPE_EXTENDED_ENTRY_DATA_MODEL = new GenericType<>() {};
    private static final GenericType<WrapperBodyDataModel<EditEntryDataModel>> GENERIC_TYPE_EDIT_ENTRY_DATA_MODEL = new GenericType<>() {};

    @Autowired
    private EntryBridgeService entryBridgeService;

    @Test
    public void shouldReturnAllEntries() throws CommunicationFailureException {

        // when
        EntryListDataModel result = entryBridgeService.getAllEntries();

        // then
        assertThat(result, notNullValue());
        assertThat(result.entries().size(), equalTo(NUMBER_OF_ALL_ENTRIES));
    }

    @ParameterizedTest
    @MethodSource("pageOfEntriesDataProvider")
    public void shouldReturnPageOfEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                          long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfEntries(page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertMenu(result);
    }

    @ParameterizedTest
    @MethodSource("pageOfPublicEntriesDataProvider")
    public void shouldReturnPageOfPublicEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntries(page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertMenu(result);
    }

    @ParameterizedTest
    @MethodSource("pageOfPublicEntriesByCategoryDataProvider")
    public void shouldReturnPageOfPublicEntriesByCategory(Long categoryID, int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                          long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByCategory(categoryID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertMenu(result);
    }

    @ParameterizedTest
    @MethodSource("pageOfPublicEntriesByTagDataProvider")
    public void shouldReturnPageOfPublicEntriesByTag(Long tagID, int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                          long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByTag(tagID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertMenu(result);
    }

    @ParameterizedTest
    @MethodSource("pageOfPublicEntriesByContentDataProvider")
    public void shouldReturnPageOfPublicEntriesByContent(String content, int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection,
                                                     long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByContent(content, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertMenu(result);
    }

    @Test
    public void shouldSearchEntriesWithEmptySearchParameters() throws CommunicationFailureException {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(25L));
        assertThat(result.pagination().entityCountOnPage(), equalTo(10));
        assertThat(result.pagination().pageCount(), equalTo(3));
    }

    @Test
    public void shouldSearchEntriesWithCategoryFilter() throws CommunicationFailureException {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        entrySearchParameters.setLimit(3);
        entrySearchParameters.setCategoryID(2L);

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(13L));
        assertThat(result.pagination().entityCountOnPage(), equalTo(3));
        assertThat(result.pagination().pageCount(), equalTo(5));
        assertThat(result.body().entries().stream().allMatch(entry -> entry.category().id() == 2L), is(true));
    }

    @Test
    public void shouldSearchEntriesWithCategoryFilterAndDisabledEntriesOnly() throws CommunicationFailureException {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        entrySearchParameters.setCategoryID(1L);
        entrySearchParameters.setEnabled(false);

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(1L));
        assertThat(result.body().entries().get(0).category().id(), is(1L));
        assertThat(result.body().entries().get(0).enabled(), is(false));
    }

    @Test
    public void shouldSearchEntriesOnlyInReviewStatus() throws CommunicationFailureException {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        entrySearchParameters.setStatus(EntryInitialStatus.REVIEW);

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(2L));
        assertThat(result.body().entries().stream().allMatch(entry -> entry.entryStatus().equals(EntryInitialStatus.REVIEW.name())), is(true));
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

    @Test
    public void shouldEntryByLinkReturnHTTP404ForNonExistingEntry() {

        // given
        String link = "entry-non-existing-1";

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> entryBridgeService.getEntryByLink(link));

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
        assertThat(entryBridgeService.getEntryByID(CONTROL_ENTRY_ID).body().enabled(), is(false));
        assertThat(entryBridgeService.getPageOfPublicEntries(1, 30, CREATED, ASC).body().entries().stream()
                .noneMatch(entryDataModel -> CONTROL_ENTRY_LINK.equals(entryDataModel.link())), is(true));
    }

    @ParameterizedTest
    @MethodSource("publicationStatusChangeDataProvider")
    @ResetDatabase
    public void shouldChangePublicationStatus(Long entryID, EntryInitialStatus newStatus) throws CommunicationFailureException {

        // when
        entryBridgeService.changePublicationStatus(entryID, newStatus);

        // then
        assertThat(entryBridgeService.getEntryByID(entryID).body().entryStatus(), is(newStatus.name()));
    }

    @ParameterizedTest
    @MethodSource("publicationStatusChangeInvalidDataProvider")
    public void shouldChangePublicationStatusInvalidScenarios(Long entryID, EntryInitialStatus newStatus) {

        // when
        Assertions.assertThrows(ConflictingRequestException.class, () -> entryBridgeService.changePublicationStatus(entryID, newStatus));

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldDeleteEntry() throws CommunicationFailureException {

        // given
        Long entryToDelete = 25L;

        // when
        entryBridgeService.deleteEntry(entryToDelete);

        // then
        // this call should result in exception
        Assertions.assertThrows(ResourceNotFoundException.class, () -> entryBridgeService.getEntryByID(entryToDelete));
    }
    
    private void assertModifiedEntry(Long entryID, EntryUpdateRequestModel expected) throws CommunicationFailureException {
        assertThat(entryBridgeService.getEntryByLink(expected.getLink()).body().rawContent(), equalTo(expected.getRawContent()));
        WrapperBodyDataModel<EditEntryDataModel> current = entryBridgeService.getEntryByID(entryID);
        assertThat(current.body().category().id(), equalTo(expected.getCategoryID()));
        assertThat(current.body().enabled(), is(expected.isEnabled()));
        assertThat(current.body().link(), equalTo(expected.getLink()));
        assertThat(current.body().locale().toLowerCase(), equalTo(expected.getLocale().getLanguage()));
        assertThat(current.body().prologue(), equalTo(expected.getPrologue()));
        assertThat(current.body().rawContent(), equalTo(expected.getRawContent()));
        assertThat(current.body().entryStatus(), equalTo(expected.getStatus().name()));
        assertThat(current.body().title(), equalTo(expected.getTitle()));
        assertThat(current.seo().metaDescription(), equalTo(expected.getMetaDescription()));
        assertThat(current.seo().metaKeywords(), equalTo(expected.getMetaKeywords()));
        assertThat(current.seo().metaTitle(), equalTo(expected.getMetaTitle()));

        if (expected instanceof EntryCreateRequestModel) {
            assertThat(current.body().user().id(), equalTo(((EntryCreateRequestModel) expected).getUserID()));
        }
    }

    private void assertPaginatedResult(WrapperBodyDataModel<EntryListDataModel> result, Comparator<EntryDataModel> comparator,
                                       long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {

        assertThat(result, notNullValue());
        assertThat(result.body().entries().size(), equalTo(expectedBodySize));
        assertThat(result.pagination(), notNullValue());
        assertThat(result.pagination().pageCount(), equalTo(expectedPageCount));
        assertThat(result.pagination().hasNext(), equalTo(expectedHasNext));
        assertThat(result.pagination().hasPrevious(), equalTo(expectedHasPrevious));
        assertThat(result.pagination().entityCount(), equalTo(expectedEntityCount));
        assertThat(result.body().entries().stream()
                .sorted(comparator)
                .toList()
                .equals(result.body().entries()), is(true));
    }

    private void assertMenu(WrapperBodyDataModel<?> result) {
        assertThat(result.menu(), equalTo(getControl(CONTROL_MENU, MenuDataModel.class)));
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

    private static Stream<Arguments> pageOfEntriesDataProvider() {
        return Stream.of(
                // page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                Arguments.of(1, 10, CREATED, ASC,  25, 10, 3, true,  false),
                Arguments.of(2, 10, CREATED, ASC,  25, 10, 3, true,  true),
                Arguments.of(3, 10, CREATED, ASC,  25, 5,  3, false, true),
                Arguments.of(1, 30, CREATED, ASC,  25, 25, 1, false, false),
                Arguments.of(1, 30, TITLE,   DESC, 25, 25, 1, false, false),
                Arguments.of(3, 8,  TITLE,   ASC,  25, 8,  4, true,  true),
                Arguments.of(2, 8,  CREATED, DESC, 25, 8,  4, true,  true)
        );
    }

    private static Stream<Arguments> pageOfPublicEntriesDataProvider() {
        return Stream.of(
                // page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                Arguments.of(1, 10, CREATED, ASC,  19, 10, 2, true,  false),
                Arguments.of(2, 10, CREATED, ASC,  19, 9,  2, false, true),
                Arguments.of(3, 10, CREATED, ASC,  19, 0,  2, false, true),
                Arguments.of(1, 30, CREATED, ASC,  19, 19, 1, false, false),
                Arguments.of(1, 30, TITLE,   DESC, 19, 19, 1, false, false),
                Arguments.of(2, 8,  TITLE,   ASC,  19, 8,  3, true,  true),
                Arguments.of(3, 8,  CREATED, DESC, 19, 3,  3, false, true)
        );
    }

    private static Stream<Arguments> pageOfPublicEntriesByCategoryDataProvider() {
        return Stream.of(
                // category ID, page, limit, order by, order direction, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                Arguments.of(1L, 1, 5,  CREATED, ASC,  8,  5,  2, true,  false),
                Arguments.of(1L, 2, 5,  CREATED, ASC,  8,  3,  2, false, true),
                Arguments.of(2L, 1, 10, CREATED, ASC,  11, 10, 2, true,  false),
                Arguments.of(2L, 2, 10, CREATED, ASC,  11, 1,  2, false, true),
                Arguments.of(1L, 2, 10, CREATED, ASC,  8,  0,  1, false, true),
                Arguments.of(2L, 1, 30, CREATED, ASC,  11, 11, 1, false, false),
                Arguments.of(1L, 1, 30, TITLE,   DESC, 8,  8,  1, false, false),
                Arguments.of(1L, 2, 3,  TITLE,   ASC,  8,  3,  3, true,  true)
        );
    }

    private static Stream<Arguments> pageOfPublicEntriesByTagDataProvider() {
        return Stream.of(
                Arguments.of(1L, 1, 5,  CREATED, ASC, 1, 1, 1, false, false),
                Arguments.of(2L, 1, 2,  CREATED, ASC, 1, 1, 1, false, false),
                Arguments.of(2L, 2, 2,  CREATED, ASC, 1, 0, 1, false, true),
                Arguments.of(0L, 1, 10, CREATED, ASC, 0, 0, 0, false, false),
                Arguments.of(9L, 1, 10, CREATED, ASC, 0, 0, 0, false, false)
        );
    }

    private static Stream<Arguments> pageOfPublicEntriesByContentDataProvider() {
        return Stream.of(
                Arguments.of("content 7",       1, 5, CREATED, ASC, 2,  2, 1, false, false),
                Arguments.of("non existing",    1, 5, CREATED, ASC, 0,  0, 0, false, false),
                Arguments.of("Prologue #25",    1, 5, CREATED, ASC, 1,  1, 1, false, false),
                Arguments.of("Entry #21 title", 1, 5, CREATED, ASC, 1,  1, 1, false, false),
                Arguments.of("content entry",   3, 5, CREATED, ASC, 19, 5, 4, true,  true)
        );
    }

    private static Stream<Arguments> publicationStatusChangeDataProvider() {

        return Stream.of(
                Arguments.of(1L, EntryInitialStatus.DRAFT),
                Arguments.of(2L, EntryInitialStatus.REVIEW),
                Arguments.of(4L, EntryInitialStatus.PUBLIC)
        );
    }

    private static Stream<Arguments> publicationStatusChangeInvalidDataProvider() {

        return Stream.of(
                Arguments.of(1L, EntryInitialStatus.REVIEW),
                Arguments.of(1L, EntryInitialStatus.PUBLIC),
                Arguments.of(2L, EntryInitialStatus.PUBLIC),
                Arguments.of(2L, EntryInitialStatus.DRAFT),
                Arguments.of(4L, EntryInitialStatus.DRAFT),
                Arguments.of(4L, EntryInitialStatus.REVIEW)
        );
    }
}
