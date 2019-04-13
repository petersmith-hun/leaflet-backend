package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.service.vo.TagVO;
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
 * Unit tests for {@link TagRouteMaskProcessor}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class TagRouteMaskProcessorTest {

    private static final String TAG_1 = "Tag 1";
    private static final String TAG_2 = "Tag 2";
    private static final TagVO TAG_VO_1 = TagVO.getBuilder()
            .withId(1L)
            .withTitle(TAG_1)
            .build();
    private static final TagVO TAG_VO_2 = TagVO.getBuilder()
            .withId(2L)
            .withTitle(TAG_2)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1 = FrontEndRouteVO.getBuilder()
            .withName(TAG_1)
            .withUrl("/tag/1/tag_1")
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2 = FrontEndRouteVO.getBuilder()
            .withName(TAG_2)
            .withUrl("/tag/2/tag_2")
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_1_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(TAG_1)
            .withUrl(StringUtils.EMPTY)
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();
    private static final FrontEndRouteVO EXPECTED_ROUTE_2_WITHOUT_URL = FrontEndRouteVO.getBuilder()
            .withName(TAG_2)
            .withUrl(StringUtils.EMPTY)
            .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
            .build();

    private static final List<TagVO> TAG_VO_LIST = Arrays.asList(TAG_VO_1, TAG_VO_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST = Arrays.asList(EXPECTED_ROUTE_1, EXPECTED_ROUTE_2);
    private static final List<FrontEndRouteVO> FRONT_END_ROUTE_VO_LIST_WITHOUT_URL = Arrays.asList(EXPECTED_ROUTE_1_WITHOUT_URL, EXPECTED_ROUTE_2_WITHOUT_URL);

    @Mock
    private TagFacade tagFacade;

    @Mock
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @InjectMocks
    private TagRouteMaskProcessor tagRouteMaskProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(filenameGeneratorUtil.doCleanFilename(TAG_1)).willReturn("tag_1");
        given(filenameGeneratorUtil.doCleanFilename(TAG_2)).willReturn("tag_2");
    }

    @Test
    @Parameters(source = SupportParameterProvider.class)
    public void shouldSupport(FrontEndRouteType type, boolean expectedResult) {

        // given
        FrontEndRouteVO frontEndRouteVO = FrontEndRouteVO.getBuilder()
                .withType(type)
                .build();

        // when
        boolean result = tagRouteMaskProcessor.supports(frontEndRouteVO);

        // then
        assertThat(result, is(expectedResult));
    }

    @Test
    public void shouldProcess() {

        // given
        given(tagFacade.getPublicTags()).willReturn(TAG_VO_LIST);
        FrontEndRouteVO routeMask = FrontEndRouteVO.getBuilder()
                .withUrl("/tag")
                .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .build();

        // when
        List<FrontEndRouteVO> result = tagRouteMaskProcessor.process(routeMask);

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST), is(true));
    }

    @Test
    public void shouldProcessWithInvalidMask() {

        // given
        given(tagFacade.getPublicTags()).willReturn(TAG_VO_LIST);

        // when
        List<FrontEndRouteVO> result = tagRouteMaskProcessor.process(FrontEndRouteVO.getBuilder()
                .withAuthRequirement(FrontEndRouteAuthRequirement.SHOW_ALWAYS)
                .build());

        // then
        assertThat(result.containsAll(FRONT_END_ROUTE_VO_LIST_WITHOUT_URL), is(true));
    }

    public static class SupportParameterProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {FrontEndRouteType.ENTRY_ROUTE_MASK,    false},
                    new Object[] {FrontEndRouteType.CATEGORY_ROUTE_MASK, false},
                    new Object[] {FrontEndRouteType.TAG_ROUTE_MASK,      true},
                    new Object[] {FrontEndRouteType.HEADER_MENU,         false},
                    new Object[] {FrontEndRouteType.FOOTER_MENU,         false},
                    new Object[] {FrontEndRouteType.STANDALONE,          false},
                    new Object[] {null,                                  false}
            };
        }
    }
}
