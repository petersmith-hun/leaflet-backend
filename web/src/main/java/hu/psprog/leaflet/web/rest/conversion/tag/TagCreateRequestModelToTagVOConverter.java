package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link TagCreateRequestModel} to {@link TagVO}.
 *
 * @author Peter Smith
 */
@Component
public class TagCreateRequestModelToTagVOConverter implements Converter<TagCreateRequestModel, TagVO> {

    @Override
    public TagVO convert(TagCreateRequestModel source) {
        return TagVO.getBuilder()
                .withTitle(source.getName())
                .build();
    }
}
