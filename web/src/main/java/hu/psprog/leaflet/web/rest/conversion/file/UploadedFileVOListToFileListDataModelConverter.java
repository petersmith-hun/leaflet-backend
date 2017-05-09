package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter {@link List} of {@link UploadedFileVO} objects to {@link FileListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class UploadedFileVOListToFileListDataModelConverter implements Converter<List<UploadedFileVO>, FileListDataModel> {

    private UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter;

    @Autowired
    public UploadedFileVOListToFileListDataModelConverter(final UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter) {
        this.uploadedFileVOToFileDataModelConverter = uploadedFileVOToFileDataModelConverter;
    }

    @Override
    public FileListDataModel convert(List<UploadedFileVO> source) {
        FileListDataModel.Builder builder = FileListDataModel.Builder.getBuilder();
        source.stream()
                .map(uploadedFileVOToFileDataModelConverter::convert)
                .forEach(builder::withItem);
        return builder.build();
    }
}
