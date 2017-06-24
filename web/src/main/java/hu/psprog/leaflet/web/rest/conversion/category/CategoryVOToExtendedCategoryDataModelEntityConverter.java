package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.ExtendedCategoryDataModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link CategoryVO} value object to {@link ExtendedCategoryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CategoryVOToExtendedCategoryDataModelEntityConverter implements Converter<CategoryVO, ExtendedCategoryDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public CategoryVOToExtendedCategoryDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ExtendedCategoryDataModel convert(CategoryVO source) {

        return ExtendedCategoryDataModel.getExtendedBuilder()
                .withDescription(source.getDescription())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withId(source.getId())
                .withTitle(source.getTitle())
                .build();
    }
}
