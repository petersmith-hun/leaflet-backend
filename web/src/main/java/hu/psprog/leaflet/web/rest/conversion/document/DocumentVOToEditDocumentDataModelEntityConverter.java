package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link DocumentVO} value object to {@link EditDocumentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToEditDocumentDataModelEntityConverter implements Converter<DocumentVO, EditDocumentDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public DocumentVOToEditDocumentDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public EditDocumentDataModel convert(DocumentVO source) {

        EditDocumentDataModel.EditDocumentDataModelBuilder builder = EditDocumentDataModel.getExtendedBuilder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withLink(source.getLink())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withRawContent(source.getRawContent())
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withEnabled(source.isEnabled())
                .withLocale(source.getLocale().name())
                .withUser(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}
