package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.impl.support.routing.RouteMaskProcessor;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link RouteMaskProcessor} implementation for tags.
 * Inserts ID and cleaned title of a tag into the route mask.
 *
 * @author Peter Smith
 */
@Component
public class TagRouteMaskProcessor implements RouteMaskProcessor {

    private static final String ROUTE_MASK = "%s/%d/%s";

    private TagFacade tagFacade;
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @Autowired
    public TagRouteMaskProcessor(TagFacade tagFacade, FilenameGeneratorUtil filenameGeneratorUtil) {
        this.tagFacade = tagFacade;
        this.filenameGeneratorUtil = filenameGeneratorUtil;
    }

    @Override
    public boolean supports(FrontEndRouteVO frontEndRouteVO) {
        return frontEndRouteVO.getType() == FrontEndRouteType.TAG_ROUTE_MASK;
    }

    @Override
    public List<FrontEndRouteVO> process(FrontEndRouteVO frontEndRouteVO) {

        return tagFacade.getPublicTags().stream()
                .map(tagVO -> FrontEndRouteVO.getBuilder()
                        .withName(tagVO.getTitle())
                        .withUrl(prepareURL(tagVO, frontEndRouteVO))
                        .withAuthRequirement(frontEndRouteVO.getAuthRequirement())
                        .build())
                .collect(Collectors.toList());
    }

    private String prepareURL(TagVO tagVO, FrontEndRouteVO frontEndRouteVO) {

        String url;
        if (Objects.nonNull(frontEndRouteVO.getUrl())) {
            url = String.format(ROUTE_MASK, frontEndRouteVO.getUrl(), tagVO.getId(), filenameGeneratorUtil.doCleanFilename(tagVO.getTitle()));
        } else {
            url = StringUtils.EMPTY;
        }

        return url;
    }
}
