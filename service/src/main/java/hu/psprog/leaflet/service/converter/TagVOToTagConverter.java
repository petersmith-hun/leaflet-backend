package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link TagVO} value object to {@link Tag} entity.
 *
 * @author Peter Smith
 */
@Component
public class TagVOToTagConverter implements Converter<TagVO, Tag> {

    @Override
    public Tag convert(TagVO source) {

        return new Tag.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .isEnabled(source.isEnabled())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .createTag();
    }
}
