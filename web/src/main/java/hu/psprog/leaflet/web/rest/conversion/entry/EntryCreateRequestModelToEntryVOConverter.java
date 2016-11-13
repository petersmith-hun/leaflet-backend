package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
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
public class EntryCreateRequestModelToEntryVOConverter implements Converter<EntryCreateRequestModel, EntryVO> {

    @Override
    public EntryVO convert(EntryCreateRequestModel entryCreateRequestModel) {

        return new EntryVO.Builder()
                .withTitle(entryCreateRequestModel.getTitle())
                .withLink(entryCreateRequestModel.getLink())
                .withPrologue(entryCreateRequestModel.getPrologue())
                .withContent(entryCreateRequestModel.getContent())
                .withOwner(UserVO.wrapMinimumVO(entryCreateRequestModel.getUserID()))
                .withCategory(CategoryVO.wrapMinimumVO(entryCreateRequestModel.getCategoryID()))
                .withEntryStatus(entryCreateRequestModel.getStatus().name())
                .withEnabled(entryCreateRequestModel.isEnabled())
                .withSeoTitle(entryCreateRequestModel.getMetaTitle())
                .withSeoDescription(entryCreateRequestModel.getMetaDescription())
                .withSeoKeywords(entryCreateRequestModel.getMetaKeywords())
                .createEntryVO();
    }
}
