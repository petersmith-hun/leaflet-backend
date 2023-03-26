package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.request.common.OrderBy;
import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.request.entry.EntrySearchParameters;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntrySearchParametersConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class EntrySearchParametersConverterTest {

    @InjectMocks
    private EntrySearchParametersConverter converter;

    @Test
    public void shouldConvertEmptyEntrySearchParametersObject() {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        EntrySearchParametersVO expectedResult = EntrySearchParametersVO.builder()
                .categoryID(Optional.empty())
                .enabled(Optional.empty())
                .status(Optional.empty())
                .content(Optional.empty())
                .page(1)
                .limit(10)
                .orderBy(EntryVO.OrderBy.CREATED)
                .orderDirection(OrderDirection.ASC)
                .build();

        // when
        EntrySearchParametersVO result = converter.convert(entrySearchParameters);

        // then
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void shouldConvertPopulatedEntrySearchParametersObject() {

        // given
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        entrySearchParameters.setCategoryID(3L);
        entrySearchParameters.setEnabled(true);
        entrySearchParameters.setStatus(EntryInitialStatus.REVIEW);
        entrySearchParameters.setContent("content1");
        entrySearchParameters.setPage(2);
        entrySearchParameters.setLimit(30);
        entrySearchParameters.setOrderBy(OrderBy.Entry.TITLE);
        entrySearchParameters.setOrderDirection(hu.psprog.leaflet.api.rest.request.common.OrderDirection.DESC);

        EntrySearchParametersVO expectedResult = EntrySearchParametersVO.builder()
                .categoryID(Optional.of(3L))
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.REVIEW))
                .content(Optional.of("content1"))
                .page(2)
                .limit(30)
                .orderBy(EntryVO.OrderBy.TITLE)
                .orderDirection(OrderDirection.DESC)
                .build();

        // when
        EntrySearchParametersVO result = converter.convert(entrySearchParameters);

        // then
        assertThat(result, equalTo(expectedResult));
    }
}
