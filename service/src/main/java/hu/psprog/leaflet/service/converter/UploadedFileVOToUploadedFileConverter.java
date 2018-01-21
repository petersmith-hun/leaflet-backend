package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an {@link UploadedFileVO} value object to {@link UploadedFile} entity.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileVOToUploadedFileConverter implements Converter<UploadedFileVO, UploadedFile> {

    @Override
    public UploadedFile convert(UploadedFileVO source) {
        return UploadedFile.getBuilder()
                .withId(source.getId())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .withPath(source.getPath())
                .withOriginalFilename(source.getOriginalFilename())
                .withMime(source.getAcceptedAs())
                .withPathUUID(source.getPathUUID())
                .withStoredFilename(source.getStoredFilename())
                .withDescription(source.getDescription())
                .build();
    }
}
