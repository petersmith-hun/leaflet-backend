package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DocumentVO} value object to {@link DocumentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToDocumentDataModelEntityConverter implements Converter<DocumentVO, DocumentDataModel> {

    private DateConverter dateConverter;

    @Autowired
    public DocumentVOToDocumentDataModelEntityConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public DocumentDataModel convert(DocumentVO source) {

        DocumentDataModel.DocumentDataModelBuilder builder = DocumentDataModel.getBuilder()
                .withRawContent(source.getRawContent())
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withLink(source.getLink())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withLocale(source.getLocale().name())
                .withUser(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}
