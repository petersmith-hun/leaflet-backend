package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an {@link UploadedFile} entity to {@link UploadedFileVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileToUploadedFileVOConverter implements Converter<UploadedFile, UploadedFileVO> {

    @Override
    public UploadedFileVO convert(UploadedFile source) {

        return UploadedFileVO.getBuilder()
                .withId(source.getId())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .withPath(source.getPath())
                .withOriginalFilename(source.getOriginalFilename())
                .withAcceptedAs(source.getMime())
                .withPathUUID(source.getPathUUID())
                .withStoredFilename(source.getStoredFilename())
                .withDescription(source.getDescription())
                .build();
    }
}
