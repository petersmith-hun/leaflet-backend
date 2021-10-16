package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.EntryFacade;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link EntryRouteMaskProcessor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntryRouteMaskProcessorTest {

    private static final String ENTRY_1 = "Entry 1";
    private static final String ENTRY_2 = "Entry 2";
    private static final EntryVO ENTRY_VO_1 = EntryVO.getBuilder()
            .withTitle(ENTRY_1)
            .withLink("entry-1")
            .build();
    private static final EntryVO ENTRY_VO_2 = EntryVO.getBuilder()
            .withTitle(ENTRY_2)
            .withLink("entry-2")
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1 = FrontEndRouteVO.getBuilder()
            .withName(ENTRY_1)
            .withUrl("/entry/entry-1")
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2 = FrontEndRouteVO.getBuilder()
            .withName(ENTRY_2)
            .withUrl("/entry/entry-2")
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(ENTRY_1)
            .withUrl(StringUtils.EMPTY)
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(ENTRY_2)
            .withUrl(StringUtils.EMPTY)
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();

    private static final List<EntryVO> ENTRY_VO_LIST = Arrays.asList(ENTRY_VO_1, ENTRY_VO_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Arrays.asList(EXPECTED_ROUTE_1, EXPECTED_ROUTE_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST_WITHOUT_URL = Arrays.asList(EXPECTED_ROUTE_1_WITHOUT_URL, EXPECTED_ROUTE_2_WITHOUT_URL);

    @Mock
    private EntryFacade entryFacade;

    @InjectMocks
    private EntryRouteMaskProcessor entryRouteMaskProcessor;

    @ParameterizedTest
    @MethodSource("supportDataProvider")
    public void shouldSupport(FrontEndRouteType type, boolean expectedResult) {

        // given
        FrontEndRouteVO frontEndRouteVO = FrontEndRouteVO.getBuilder()
                .withType(type)
                .build();

        // when
        boolean result = entryRouteMaskProcessor.supports(frontEndRouteVO);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldProcess() {

        // given
        given(entryFacade.getListOfPublicEntries()).willReturn(ENTRY_VO_LIST);
        FrontEndRouteVO routeMask = FrontEndRouteVO.getBuilder()
                .withUrl("/entry")
                .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .build();

        // when
        List<FrontEndRouteVO> result = entryRouteMaskProcessor.process(routeMask);

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST), is(true));
    }

    @Test
    public void shouldProcessWithInvalidMask() {

        // given
        given(entryFacade.getListOfPublicEntries()).willReturn(ENTRY_VO_LIST);

        // when
        List<FrontEndRouteVO> result = entryRouteMaskProcessor.process(FrontEndRouteVO.getBuilder()
                .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .build());

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST_WITHOUT_URL), is(true));
    }

    public static Stream<Arguments> supportDataProvider() {
        
        return Stream.of(
                Arguments.of(FrontEndRouteType.ENTRY_ROUTE_MASK,    true),
                Arguments.of(FrontEndRouteType.CATEGORY_ROUTE_MASK, false),
                Arguments.of(FrontEndRouteType.TAG_ROUTE_MASK,      false),
                Arguments.of(FrontEndRouteType.HEADER_MENU,         false),
                Arguments.of(FrontEndRouteType.FOOTER_MENU,         false),
                Arguments.of(FrontEndRouteType.STANDALONE,          false),
                Arguments.of(null,                                  false)
        );
    }
}