package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CategoryVO} value object to {@link Category} entity;
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToCategoryConverter implements Converter<CategoryVO, Category> {

    @Override
    public Category convert(CategoryVO source) {

        return Category.getBuilder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .build();
    }
}
