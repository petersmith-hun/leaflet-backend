package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Attachment;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link AttachmentVO} value object to {@link Attachment} entity.
 *
 * @author Peter Smith
 */
@Component
public class AttachmentVOToAttachmentConverter implements Converter<AttachmentVO, Attachment> {

    @Autowired
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Override
    public Attachment convert(AttachmentVO source) {

        return new Attachment.Builder()
                .withId(source.getId())
                .withTitle(source.getTitle())
                .withDescription(source.getDescription())
                .withFilename(source.getFilename())
                .withType(source.getType())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .isEnabled(source.isEnabled())
                .isProtected(source.getIsProtected())
                .withEntry(entryVOToEntryConverter.convert(source.getEntryVO()))
                .createAttachment();
    }
}
