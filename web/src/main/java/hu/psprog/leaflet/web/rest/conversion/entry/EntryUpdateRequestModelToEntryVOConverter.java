package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.JULocaleToLeafletLocaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link EntryCreateRequestModel} model to {@link EntryVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class EntryUpdateRequestModelToEntryVOConverter implements Converter<EntryUpdateRequestModel, EntryVO> {

    private final JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter;

    @Autowired
    public EntryUpdateRequestModelToEntryVOConverter(JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter) {
        this.juLocaleToLeafletLocaleConverter = juLocaleToLeafletLocaleConverter;
    }

    @Override
    public EntryVO convert(EntryUpdateRequestModel entryUpdateRequestModel) {

        EntryVO.EntryVOBuilder<?, ?> builder = EntryVO.getBuilder()
                .withTitle(entryUpdateRequestModel.getTitle())
                .withLink(entryUpdateRequestModel.getLink())
                .withPrologue(entryUpdateRequestModel.getPrologue())
                .withRawContent(entryUpdateRequestModel.getRawContent())
                .withCategory(CategoryVO.wrapMinimumVO(entryUpdateRequestModel.getCategoryID()))
                .withEnabled(entryUpdateRequestModel.isEnabled())
                .withSeoTitle(entryUpdateRequestModel.getMetaTitle())
                .withSeoDescription(entryUpdateRequestModel.getMetaDescription())
                .withSeoKeywords(entryUpdateRequestModel.getMetaKeywords())
                .withEntryStatus(entryUpdateRequestModel.getStatus().name())
                .withLocale(juLocaleToLeafletLocaleConverter.convert(entryUpdateRequestModel.getLocale()));

        if (entryUpdateRequestModel instanceof EntryCreateRequestModel) {
            builder.withOwner(UserVO.wrapMinimumVO(((EntryCreateRequestModel) entryUpdateRequestModel).getUserID()));
        }

        return builder.build();
    }
}
