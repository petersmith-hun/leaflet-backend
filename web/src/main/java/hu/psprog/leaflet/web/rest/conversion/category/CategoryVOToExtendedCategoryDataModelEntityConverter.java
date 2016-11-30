package hu.psprog.leaflet.web.rest.conversion.category;

import hu.psprog.leaflet.api.rest.response.category.ExtendedCategoryDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
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
public class CategoryVOToExtendedCategoryDataModelEntityConverter implements Converter<CategoryVO, BaseBodyDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public BaseBodyDataModel convert(CategoryVO source) {

        return new ExtendedCategoryDataModel.Builder()
                .withDescription(source.getDescription())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withID(source.getId())
                .withTitle(source.getTitle())
                .build();
    }
}