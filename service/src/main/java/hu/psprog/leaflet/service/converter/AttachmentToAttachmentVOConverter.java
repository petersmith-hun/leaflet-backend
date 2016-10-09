package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Attachment} entity to {@link AttachmentVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class AttachmentToAttachmentVOConverter implements Converter<Attachment, AttachmentVO> {

    @Autowired
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @Override
    public AttachmentVO convert(Attachment source) {

        return new AttachmentVO.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withFilename(source.getFilename())
                .withType(source.getType())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .withIsProtected(source.isProtected())
                .withEntryVO(entryToEntryVOConverter.convert(source.getEntry()))
                .createAttachmentVO();
    }
}
