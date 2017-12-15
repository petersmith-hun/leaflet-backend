package hu.psprog.leaflet.acceptance.suits;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuit;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

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
@LeafletAcceptanceSuit
public class EntriesControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_ENTRIES = 25;

    private static final Comparator<EntryDataModel> ASC_BY_CREATED = Comparator.comparing(EntryDataModel::getCreated);
    private static final Comparator<EntryDataModel> DESC_BY_CREATED = Comparator.comparing(EntryDataModel::getCreated).reversed();
    private static final Comparator<EntryDataModel> ASC_BY_TITLE = Comparator.comparing(EntryDataModel::getTitle);
    private static final Comparator<EntryDataModel> DESC_BY_TITLE = Comparator.comparing(EntryDataModel::getTitle).reversed();

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
                    // page, limit, order by, order desc, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
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
                    // page, limit, order by, order desc, exp. all entry, exp. body size, exp. num. of pages, exp. has next, exp. has previous
                    new Object[] {1, 10, CREATED, ASC,  19, 10, 2, true,  false},
                    new Object[] {2, 10, CREATED, ASC,  19, 9,  2, false, true},
                    new Object[] {3, 10, CREATED, ASC,  19, 0,  2, false, true},
                    new Object[] {1, 30, CREATED, ASC,  19, 19, 1, false, false},
                    new Object[] {1, 30, TITLE,   DESC, 19, 19, 1, false, false},
                    new Object[] {2, 8,  TITLE,   ASC,  19, 8,  3, true,  true},
                    new Object[] {3, 8,  CREATED, DESC, 19, 3,  3, false,  true},
            };
        }
    }
}
