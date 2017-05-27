package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.service.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link TagVO} objects to {@link TagListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class TagVOToTagDataModelListConverter implements Converter<List<TagVO>, TagListDataModel> {

    private TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter;

    @Autowired
    public TagVOToTagDataModelListConverter(TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter) {
        this.tagVOToTagDataModelEntityConverter = tagVOToTagDataModelEntityConverter;
    }

    @Override
    public TagListDataModel convert(List<TagVO> source) {
        TagListDataModel.Builder builder = TagListDataModel.Builder.getBuilder();
        source.stream()
                .map(tagVOToTagDataModelEntityConverter::convert)
                .forEach(builder::withItem);
        return builder.build();
    }
}
