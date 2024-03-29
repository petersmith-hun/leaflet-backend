package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link CategoryVO} value objects to {@link CategoryListDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToCategoryDataModelListConverter implements Converter<List<CategoryVO>, CategoryListDataModel> {

    private final CategoryVOToCategoryDataModelEntityConverter categoryVOToCategoryDataModelEntityConverter;

    @Autowired
    public CategoryVOToCategoryDataModelListConverter(CategoryVOToCategoryDataModelEntityConverter categoryVOToCategoryDataModelEntityConverter) {
        this.categoryVOToCategoryDataModelEntityConverter = categoryVOToCategoryDataModelEntityConverter;
    }

    @Override
    public CategoryListDataModel convert(List<CategoryVO> source) {

        return CategoryListDataModel.getBuilder()
                .withCategories(source.stream()
                        .map(categoryVOToCategoryDataModelEntityConverter::convert)
                        .toList())
                .build();
    }
}
