package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CategoryVO} value object to {@link CategoryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToCategoryDataModelEntityConverter implements Converter<CategoryVO, CategoryDataModel> {

    private final DateConverter dateConverter;

    @Autowired
    public CategoryVOToCategoryDataModelEntityConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public CategoryDataModel convert(CategoryVO source) {

        return CategoryDataModel.getBuilder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .withEnabled(source.isEnabled())
                .build();
    }
}
