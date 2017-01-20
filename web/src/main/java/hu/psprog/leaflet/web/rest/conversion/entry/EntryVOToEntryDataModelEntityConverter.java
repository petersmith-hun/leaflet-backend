package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link EntryVO} value object to {@link EntryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEntryDataModelEntityConverter implements Converter<EntryVO, EntryDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public EntryDataModel convert(EntryVO entryVO) {

        EntryDataModel.Builder builder = new EntryDataModel.Builder()
                .withID(entryVO.getId())
                .withTitle(entryVO.getTitle())
                .withLink(entryVO.getLink())
                .withPrologue(entryVO.getPrologue())
                .withCreated(commonFormatter.formatDate(entryVO.getCreated(), httpServletRequest.getLocale()))
                .withCategory(new CategoryDataModel.Builder()
                        .withTitle(entryVO.getCategory().getTitle())
                        .withID(entryVO.getCategory().getId())
                        .build())
                .withOwner(new UserDataModel.Builder()
                        .withID(entryVO.getOwner().getId())
                        .withUsername(entryVO.getOwner().getUsername())
                        .build());

        // TODO add tags

        return builder.build();
    }
}
