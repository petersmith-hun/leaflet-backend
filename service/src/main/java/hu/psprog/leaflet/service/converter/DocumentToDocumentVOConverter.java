package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class DocumentToDocumentVOConverter implements Converter<Document, DocumentVO> {

    @Override
    public DocumentVO convert(Document source) {
        return null;
    }
}
