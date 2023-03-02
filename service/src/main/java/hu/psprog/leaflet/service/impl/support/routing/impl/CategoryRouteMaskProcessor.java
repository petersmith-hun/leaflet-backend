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
 * Inserts ID and cleaned title of a category into the route mask.
 *
 * @author Peter Smith
 */
@Component
public class CategoryRouteMaskProcessor implements RouteMaskProcessor {

    private static final String ROUTE_MASK = "%s/%d/%s";

    private final CategoryFacade categoryFacade;
    private final FilenameGeneratorUtil filenameGeneratorUtil;

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
                        .withAuthRequirement(frontEndRouteVO.getAuthRequirement())
                        .build())
                .collect(Collectors.toList());
    }

    private String prepareURL(CategoryVO categoryVO, FrontEndRouteVO frontEndRouteVO) {

        return Objects.nonNull(frontEndRouteVO.getUrl())
                ? String.format(ROUTE_MASK, frontEndRouteVO.getUrl(), categoryVO.getId(), filenameGeneratorUtil.doCleanFilename(categoryVO.getTitle()))
                : StringUtils.EMPTY;

    }
}
