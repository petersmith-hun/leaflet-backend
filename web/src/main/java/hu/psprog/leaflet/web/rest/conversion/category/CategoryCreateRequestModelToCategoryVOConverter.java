package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CategoryCreateRequestModel} request model to {@link CategoryVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class CategoryCreateRequestModelToCategoryVOConverter implements Converter<CategoryCreateRequestModel, CategoryVO> {

    @Override
    public CategoryVO convert(CategoryCreateRequestModel source) {

        return CategoryVO.getBuilder()
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .build();
    }
}
