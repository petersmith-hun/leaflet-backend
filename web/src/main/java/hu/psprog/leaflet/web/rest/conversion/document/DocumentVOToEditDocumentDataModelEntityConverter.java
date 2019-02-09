package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DocumentVO} value object to {@link EditDocumentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToEditDocumentDataModelEntityConverter implements Converter<DocumentVO, EditDocumentDataModel> {

    private DateConverter dateConverter;

    @Autowired
    public DocumentVOToEditDocumentDataModelEntityConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public EditDocumentDataModel convert(DocumentVO source) {

        EditDocumentDataModel.EditDocumentDataModelBuilder builder = EditDocumentDataModel.getExtendedBuilder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withLink(source.getLink())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withRawContent(source.getRawContent())
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .withEnabled(source.isEnabled())
                .withLocale(source.getLocale().name())
                .withUser(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}
