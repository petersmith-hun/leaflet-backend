package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.CategoryFacade;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link CategoryRouteMaskProcessor}.
 * 
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class CategoryRouteMaskProcessorTest {


    private static final String CATEGORY_1 = "Category 1";
    private static final String CATEGORY_2 = "Category 2";
    private static final CategoryVO CATEGORY_VO_1 = CategoryVO.getBuilder()
            .withId(1L)
            .withTitle(CATEGORY_1)
            .build();
    private static final CategoryVO CATEGORY_VO_2 = CategoryVO.getBuilder()
            .withId(2L)
            .withTitle(CATEGORY_2)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1 = FrontEndRouteVO.getBuilder()
            .withName(CATEGORY_1)
            .withUrl("/category/category_1/1")
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2 = FrontEndRouteVO.getBuilder()
            .withName(CATEGORY_2)
            .withUrl("/category/category_2/2")
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(CATEGORY_1)
            .withUrl(StringUtils.EMPTY)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(CATEGORY_2)
            .withUrl(StringUtils.EMPTY)
            .build();

    private static final List<CategoryVO> CATEGORY_VO_LIST = Arrays.asList(CATEGORY_VO_1, CATEGORY_VO_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Arrays.asList(EXPECTED_ROUTE_1, EXPECTED_ROUTE_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST_WITHOUT_URL = Arrays.asList(EXPECTED_ROUTE_1_WITHOUT_URL, EXPECTED_ROUTE_2_WITHOUT_URL);

    @Mock
    private CategoryFacade categoryFacade;

    @Mock
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @InjectMocks
    private CategoryRouteMaskProcessor categoryRouteMaskProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(filenameGeneratorUtil.doCleanFilename(CATEGORY_1)).willReturn("category_1");
        given(filenameGeneratorUtil.doCleanFilename(CATEGORY_2)).willReturn("category_2");
    }

    @Test
    @Parameters(source = SupportParameterProvider.class)
    public void shouldSupport(FrontEndRouteType type, boolean expectedResult) {

        // given
        FrontEndRouteVO frontEndRouteVO = FrontEndRouteVO.getBuilder()
                .withType(type)
                .build();

        // when
        boolean result = categoryRouteMaskProcessor.supports(frontEndRouteVO);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldProcess() {

        // given
        given(categoryFacade.getAllPublic()).willReturn(CATEGORY_VO_LIST);
        FrontEndRouteVO routeMask = FrontEndRouteVO.getBuilder()
                .withUrl("/category")
                .build();

        // when
        List<FrontEndRouteVO> result = categoryRouteMaskProcessor.process(routeMask);

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST), is(true));
    }

    @Test
    public void shouldProcessWithInvalidMask() {

        // given
        given(categoryFacade.getAllPublic()).willReturn(CATEGORY_VO_LIST);

        // when
        List<FrontEndRouteVO> result = categoryRouteMaskProcessor.process(FrontEndRouteVO.getBuilder().build());

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST_WITHOUT_URL), is(true));
    }

    public static class SupportParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {FrontEndRouteType.ENTRY_ROUTE_MASK,    false},
                    new Object[] {FrontEndRouteType.CATEGORY_ROUTE_MASK, true},
                    new Object[] {FrontEndRouteType.HEADER_MENU,         false},
                    new Object[] {FrontEndRouteType.FOOTER_MENU,         false},
                    new Object[] {FrontEndRouteType.STANDALONE,          false},
                    new Object[] {null,                                  false}
            };
        }
    }
}