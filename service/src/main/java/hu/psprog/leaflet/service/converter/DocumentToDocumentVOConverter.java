package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Document} to {@link DocumentVO} object.
 *
 * @author Peter Smith
 */
@Component
public class DocumentToDocumentVOConverter implements Converter<Document, DocumentVO> {

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    @Override
    public DocumentVO convert(Document source) {

        DocumentVO.Builder builder = new DocumentVO.Builder();
        builder.withContent(source.getContent())
                .withRawContent(source.getRawContent())
                .withCreated(source.getCreated())
                .withEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (source.getUser() != null) {
            builder.withOwner(userToUserVOConverter.convert(source.getUser()));
        }

        return builder.createDocumentVO();
    }
}
