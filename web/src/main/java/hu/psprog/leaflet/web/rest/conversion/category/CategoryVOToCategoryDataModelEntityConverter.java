package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CategoryVO} value object to {@link CategoryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToCategoryDataModelEntityConverter implements Converter<CategoryVO, BaseBodyDataModel> {

    @Override
    public BaseBodyDataModel convert(CategoryVO source) {

        return new CategoryDataModel.Builder()
                .withID(source.getId())
                .withTitle(source.getTitle())
                .build();
    }
}
