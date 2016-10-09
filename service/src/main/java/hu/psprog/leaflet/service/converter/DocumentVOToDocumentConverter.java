package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link DocumentVO} value object to {@link Document} entity.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToDocumentConverter implements Converter<DocumentVO, Document> {

    @Autowired
    private UserVOToUserConverter userVOToUserConverter;

    @Override
    public Document convert(DocumentVO source) {

        Document.Builder builder = new Document.Builder();
        builder.withContent(source.getContent())
                .withCreated(source.getCreated())
                .isEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (source.getOwner() != null) {
            builder.withUser(userVOToUserConverter.convert(source.getOwner()));
        }

        return builder.createDocument();
    }
}
