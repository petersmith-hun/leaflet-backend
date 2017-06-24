package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.request.file.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link UpdateFileMetaInfoRequestModel} to {@link UpdateFileMetaInfoVO}.
 *
 * @author Peter Smith
 */
@Component
public class UpdateFileMetaInfoRequestModelToUpdateFileMetaInfoVOConverter implements Converter<UpdateFileMetaInfoRequestModel, UpdateFileMetaInfoVO> {

    @Override
    public UpdateFileMetaInfoVO convert(UpdateFileMetaInfoRequestModel source) {
        return UpdateFileMetaInfoVO.getBuilder()
                .withOriginalFilename(source.getOriginalFilename())
                .withDescription(source.getDescription())
                .build();
    }
}
