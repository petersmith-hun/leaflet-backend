package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link EntryCreateRequestModel} model to {@link EntryVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class EntryUpdateRequestModelToEntryVOConverter implements Converter<EntryUpdateRequestModel, EntryVO> {

    @Override
    public EntryVO convert(EntryUpdateRequestModel entryUpdateRequestModel) {

        EntryVO.Builder builder = new EntryVO.Builder()
                .withTitle(entryUpdateRequestModel.getTitle())
                .withLink(entryUpdateRequestModel.getLink())
                .withPrologue(entryUpdateRequestModel.getPrologue())
                .withContent(entryUpdateRequestModel.getContent())
                .withRawContent(entryUpdateRequestModel.getRawContent())
                .withCategory(CategoryVO.wrapMinimumVO(entryUpdateRequestModel.getCategoryID()))
                .withEnabled(entryUpdateRequestModel.isEnabled())
                .withSeoTitle(entryUpdateRequestModel.getMetaTitle())
                .withSeoDescription(entryUpdateRequestModel.getMetaDescription())
                .withSeoKeywords(entryUpdateRequestModel.getMetaKeywords())
                .withEntryStatus(entryUpdateRequestModel.getStatus().name());

        if (entryUpdateRequestModel instanceof EntryCreateRequestModel) {
            builder.withOwner(UserVO.wrapMinimumVO(((EntryCreateRequestModel) entryUpdateRequestModel).getUserID()))
                    .withEntryStatus(((EntryCreateRequestModel) entryUpdateRequestModel).getStatus().name());
        }

        return builder.createEntryVO();
    }
}
