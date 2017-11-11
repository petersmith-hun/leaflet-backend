package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link CategoryVO} value object to {@link CategoryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToCategoryDataModelEntityConverter implements Converter<CategoryVO, CategoryDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public CategoryVOToCategoryDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public CategoryDataModel convert(CategoryVO source) {

        return CategoryDataModel.getBuilder()
                .withID(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withEnabled(source.isEnabled())
                .build();
    }
}
