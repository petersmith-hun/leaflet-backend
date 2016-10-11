package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Tag} entity to {@link TagVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class TagToTagVOConverter implements Converter<Tag, TagVO> {

    @Override
    public TagVO convert(Tag source) {

        return new TagVO.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withEnabled(source.isEnabled())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .createTagVO();
    }
}
