package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link UploadedFileVO} to {@link FileDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileVOToFileDataModelConverter implements Converter<UploadedFileVO, FileDataModel> {

    private static final String REFERENCE_URL_PATTERN = "/%s/%s";

    @Override
    public FileDataModel convert(UploadedFileVO source) {
        return FileDataModel.getBuilder()
                .withOriginalFilename(source.getOriginalFilename())
                .withReference(buildReference(source))
                .withAcceptedAs(source.getAcceptedAs())
                .withDescription(source.getDescription())
                .build();
    }

    private String buildReference(UploadedFileVO source) {
        return String.format(REFERENCE_URL_PATTERN, source.getPathUUID().toString(), source.getStoredFilename());
    }
}
