package hu.psprog.leaflet.web.rest.conversion.sitemap;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.api.rest.response.sitemap.SitemapLocationItem;
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

        return Sitemap.getBuilder()
                .withSitemapLocationItemList(source.stream()
                        .map(FrontEndRouteVO::getUrl)
                        .map(SitemapLocationItem::new)
                        .toList())
                .build();
    }
}
