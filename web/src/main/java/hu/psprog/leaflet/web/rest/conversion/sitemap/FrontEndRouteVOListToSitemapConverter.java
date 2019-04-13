package hu.psprog.leaflet.web.rest.conversion.sitemap;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts a {@link List} of {@link FrontEndRouteVO} objects to {@link Sitemap}.
 *
 * @author Peter Smith
 */
@Component
public class FrontEndRouteVOListToSitemapConverter implements Converter<List<FrontEndRouteVO>, Sitemap> {

    @Override
    public Sitemap convert(List<FrontEndRouteVO> source) {

        Sitemap.SitemapBuilder builder = Sitemap.getBuilder();
        source.forEach(route -> builder.withLocation(route.getUrl()));

        return builder.build();
    }
}
