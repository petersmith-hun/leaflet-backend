package hu.psprog.leaflet.web.rest.conversion.tag;

import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link TagAssignmentRequestModel} to {@link TagAssignmentVO}.
 *
 * @author Peter Smith
 */
@Component
public class TagAssignmentRequestConverter implements Converter<TagAssignmentRequestModel, TagAssignmentVO> {

    @Override
    public TagAssignmentVO convert(TagAssignmentRequestModel source) {
        return TagAssignmentVO.getBuilder()
                .withEntryID(source.getEntryID())
                .withTagID(source.getTagID())
                .build();
    }
}
