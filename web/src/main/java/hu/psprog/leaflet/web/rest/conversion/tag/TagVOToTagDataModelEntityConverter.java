package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link TagVO} to {@link TagDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class TagVOToTagDataModelEntityConverter implements Converter<TagVO, TagDataModel> {

    private DateConverter dateConverter;

    @Autowired
    public TagVOToTagDataModelEntityConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public TagDataModel convert(TagVO source) {
        return TagDataModel.getBuilder()
                .withId(source.getId())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withEnabled(source.isEnabled())
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .withName(source.getTitle())
                .build();
    }
}
