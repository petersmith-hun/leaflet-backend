package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.CategoryFacade;
import hu.psprog.leaflet.service.impl.support.routing.RouteMaskProcessor;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link RouteMaskProcessor} for category routes.
 * Inserts cleaned title and ID of a category into the route mask.
 *
 * @author Peter Smith
 */
@Component
public class CategoryRouteMaskProcessor implements RouteMaskProcessor {

    private static final String ROUTE_MASK = "%s/%s/%s";

    private CategoryFacade categoryFacade;
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @Autowired
    public CategoryRouteMaskProcessor(CategoryFacade categoryFacade, FilenameGeneratorUtil filenameGeneratorUtil) {
        this.categoryFacade = categoryFacade;
        this.filenameGeneratorUtil = filenameGeneratorUtil;
    }

    @Override
    public boolean supports(FrontEndRouteVO frontEndRouteVO) {
        return frontEndRouteVO.getType() == FrontEndRouteType.CATEGORY_ROUTE_MASK;
    }

    @Override
    public List<FrontEndRouteVO> process(FrontEndRouteVO frontEndRouteVO) {

        return categoryFacade.getAllPublic().stream()
                .map(categoryVO -> FrontEndRouteVO.getBuilder()
                        .withName(categoryVO.getTitle())
                        .withUrl(prepareURL(categoryVO, frontEndRouteVO))
                        .build())
                .collect(Collectors.toList());
    }

    private String prepareURL(CategoryVO categoryVO, FrontEndRouteVO frontEndRouteVO) {

        String url;
        if (Objects.nonNull(frontEndRouteVO.getUrl())) {
            url = String.format(ROUTE_MASK, frontEndRouteVO.getUrl(), filenameGeneratorUtil.doCleanFilename(categoryVO.getTitle()), categoryVO.getId().toString());
        } else {
            url = StringUtils.EMPTY;
        }

        return url;
    }
}
