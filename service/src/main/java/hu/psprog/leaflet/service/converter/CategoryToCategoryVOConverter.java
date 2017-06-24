package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.service.vo.CategoryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Category} entity to {@link CategoryVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class CategoryToCategoryVOConverter implements Converter<Category, CategoryVO> {

    @Override
    public CategoryVO convert(Category source) {

        return CategoryVO.getBuilder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .build();
    }
}
