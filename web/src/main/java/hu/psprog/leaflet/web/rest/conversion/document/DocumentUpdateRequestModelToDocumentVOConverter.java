package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.JULocaleToLeafletLocaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DocumentUpdateRequestModel} model to {@link DocumentVO} value object.
 * If given source is an instance of {@link DocumentCreateRequestModel}, then its parameters will be added to the destination VO.
 *
 * @author Peter Smith
 */
@Component
public class DocumentUpdateRequestModelToDocumentVOConverter implements Converter<DocumentUpdateRequestModel, DocumentVO> {

    private JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter;

    @Autowired
    public DocumentUpdateRequestModelToDocumentVOConverter(JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter) {
        this.juLocaleToLeafletLocaleConverter = juLocaleToLeafletLocaleConverter;
    }

    @Override
    public DocumentVO convert(DocumentUpdateRequestModel source) {

        DocumentVO.DocumentVOBuilder builder = DocumentVO.getBuilder()
                .withTitle(source.getTitle())
                .withContent(source.getContent())
                .withRawContent(source.getRawContent())
                .withLink(source.getLink())
                .withEnabled(source.isEnabled())
                .withLocale(juLocaleToLeafletLocaleConverter.convert(source.getLocale()))
                .withSeoTitle(source.getMetaTitle())
                .withSeoDescription(source.getMetaDescription())
                .withSeoKeywords(source.getMetaKeywords());

        if (source instanceof DocumentCreateRequestModel) {
            builder.withOwner(UserVO.wrapMinimumVO(((DocumentCreateRequestModel) source).getUserID()));
        }

        return builder.build();
    }
}
