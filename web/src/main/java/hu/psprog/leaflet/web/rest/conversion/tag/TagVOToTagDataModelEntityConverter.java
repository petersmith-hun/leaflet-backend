package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link TagVO} to {@link TagDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class TagVOToTagDataModelEntityConverter implements Converter<TagVO, TagDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public TagVOToTagDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public TagDataModel convert(TagVO source) {
        return TagDataModel.Builder.getBuilder()
                .withId(source.getId())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withIsEnabled(source.isEnabled())
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withName(source.getTitle())
                .build();
    }
}
