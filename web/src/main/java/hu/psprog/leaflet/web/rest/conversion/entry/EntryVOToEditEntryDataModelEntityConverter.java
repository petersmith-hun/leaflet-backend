package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link EntryVO} value object to {@link EditEntryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEditEntryDataModelEntityConverter implements Converter<EntryVO, EditEntryDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public EditEntryDataModel convert(EntryVO entryVO) {

        EditEntryDataModel.Builder builder = new EditEntryDataModel.Builder()
                .withRawContent(entryVO.getRawContent())
                .withLastModified(commonFormatter.formatDate(entryVO.getLastModified(), httpServletRequest.getLocale()))
                .withTitle(entryVO.getTitle())
                .withLink(entryVO.getLink())
                .withPrologue(entryVO.getPrologue())
                .withCreated(commonFormatter.formatDate(entryVO.getCreated(), httpServletRequest.getLocale()))
                .withID(entryVO.getId())
                .withOwner(new UserDataModel.Builder()
                        .withID(entryVO.getOwner().getId())
                        .withUsername(entryVO.getOwner().getUsername())
                        .build());

        return builder.build();
    }
}
