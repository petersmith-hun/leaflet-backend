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

    private static final String LIST_NODE_NAME = "categories";

    @Autowired
    private CategoryVOToCategoryDataModelEntityConverter categoryVOToCategoryDataModelEntityConverter;

    @Override
    public CategoryListDataModel convert(List<CategoryVO> source) {

        CategoryListDataModel.CategoryListDataModelBuilder builder = CategoryListDataModel.getBuilder();
        source.forEach(category -> builder.withItem(categoryVOToCategoryDataModelEntityConverter.convert(category)));

        return builder.build();
    }
}
