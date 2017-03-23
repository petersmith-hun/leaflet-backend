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

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public EditDocumentDataModel convert(DocumentVO source) {

        EditDocumentDataModel.Builder builder = new EditDocumentDataModel.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withLink(source.getLink())
                .withContent(source.getContent())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withRawContent(source.getRawContent())
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withEnabled(source.isEnabled())
                .withUser(new UserDataModel.Builder()
                        .withID(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}