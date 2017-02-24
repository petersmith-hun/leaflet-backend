package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link DocumentVO} value object to {@link DocumentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToDocumentDataModelEntityConverter implements Converter<DocumentVO, DocumentDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public DocumentDataModel convert(DocumentVO source) {

        DocumentDataModel.Builder builder = new DocumentDataModel.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withLink(source.getLink())
                .withContent(source.getContent())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withUser(new UserDataModel.Builder()
                        .withID(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}
