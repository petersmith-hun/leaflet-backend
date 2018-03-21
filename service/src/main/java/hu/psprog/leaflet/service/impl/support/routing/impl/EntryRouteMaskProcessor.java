package hu.psprog.leaflet.service.impl.support.routing.impl;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.facade.EntryFacade;
import hu.psprog.leaflet.service.impl.support.routing.RouteMaskProcessor;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link RouteMaskProcessor} for entry routes.
 * Inserts link of an entry into the route mask.
 *
 * @author Peter Smith
 */
@Component
public class EntryRouteMaskProcessor implements RouteMaskProcessor {

    private EntryFacade entryFacade;

    @Autowired
    public EntryRouteMaskProcessor(EntryFacade entryFacade) {
        this.entryFacade = entryFacade;
    }

    @Override
    public boolean supports(FrontEndRouteVO frontEndRouteVO) {

        return frontEndRouteVO.getType() == FrontEndRouteType.ENTRY_ROUTE_MASK;
    }

    @Override
    public List<FrontEndRouteVO> process(FrontEndRouteVO frontEndRouteVO) {

        return entryFacade.getListOfPublicEntries().stream()
                .map(entryVO -> FrontEndRouteVO.getBuilder()
                        .withName(entryVO.getTitle())
                        .withUrl(prepareURL(entryVO, frontEndRouteVO))
                        .build())
                .collect(Collectors.toList());
    }

    private String prepareURL(EntryVO entryVO, FrontEndRouteVO frontEndRouteVO) {

        String url;
        if (Objects.nonNull(frontEndRouteVO.getUrl())) {
            url = String.format(frontEndRouteVO.getUrl(), entryVO.getLink());
        } else {
            url = StringUtils.EMPTY;
        }

        return url;
    }
}
