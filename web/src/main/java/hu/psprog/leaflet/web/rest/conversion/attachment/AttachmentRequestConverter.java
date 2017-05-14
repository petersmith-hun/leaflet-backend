package hu.psprog.leaflet.web.rest.conversion.attachment;

import hu.psprog.leaflet.api.rest.request.attachment.AttachmentRequestModel;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link AttachmentRequestModel} to {@link AttachmentRequestVO}.
 *
 * @author Peter Smith
 */
@Component
public class AttachmentRequestConverter implements Converter<AttachmentRequestModel, AttachmentRequestVO> {

    @Override
    public AttachmentRequestVO convert(AttachmentRequestModel source) {
        return AttachmentRequestVO.Builder.getBuilder()
                .withEntryID(source.getEntryID())
                .withPathUUID(source.getPathUUID())
                .build();
    }
}
