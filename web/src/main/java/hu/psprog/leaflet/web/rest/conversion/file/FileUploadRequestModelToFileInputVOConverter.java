package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.service.vo.FileInputVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Converts {@link FileUploadRequestModel} to {@link FileInputVO}.
 *
 * @author Peter Smith
 */
@Component
public class FileUploadRequestModelToFileInputVOConverter implements Converter<FileUploadRequestModel, FileInputVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadRequestModelToFileInputVOConverter.class);

    @Override
    public FileInputVO convert(FileUploadRequestModel source) {

        InputStream inputStream;
        try {
            inputStream = source.getInputFile().getInputStream();
        } catch (IOException e) {
            LOGGER.error("Input file stream is not accessible.", e);
            throw new IllegalStateException(e);
        }

        return FileInputVO.Builder.getBuilder()
                .withContentType(source.getInputFile().getContentType())
                .withOriginalFilename(source.getInputFile().getOriginalFilename())
                .withSize(source.getInputFile().getSize())
                .withFileContentStream(inputStream)
                .withRelativePath(source.getSubFolder())
                .withDescription(source.getDescription())
                .build();
    }
}
