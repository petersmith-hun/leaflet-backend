package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts {@link Document} to {@link DocumentVO} object.
 *
 * @author Peter Smith
 */
@Component
public class DocumentToDocumentVOConverter implements Converter<Document, DocumentVO> {

    private final UserToUserVOConverter userToUserVOConverter;

    @Autowired
    public DocumentToDocumentVOConverter(UserToUserVOConverter userToUserVOConverter) {
        this.userToUserVOConverter = userToUserVOConverter;
    }

    @Override
    public DocumentVO convert(Document source) {

        DocumentVO.DocumentVOBuilder<?, ?> builder = DocumentVO.getBuilder();
        builder.withRawContent(source.getRawContent())
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

        if (Objects.nonNull(source.getUser())) {
            builder.withOwner(userToUserVOConverter.convert(source.getUser()));
        }

        return builder.build();
    }
}
