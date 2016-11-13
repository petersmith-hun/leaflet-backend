package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link EntryVO} value object to {@link ExtendedEntryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToExtendedEntryDataModelEntityConverter implements Converter<EntryVO, BaseBodyDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public BaseBodyDataModel convert(EntryVO entryVO) {

        EntryDataModel.Builder builder = new ExtendedEntryDataModel.Builder()
                .withContent(entryVO.getContent())
                .withLastModified(commonFormatter.formatDate(entryVO.getLastModified(), httpServletRequest.getLocale()))
                .withTitle(entryVO.getTitle())
                .withLink(entryVO.getLink())
                .withPrologue(entryVO.getPrologue())
                .withCreated(commonFormatter.formatDate(entryVO.getCreated(), httpServletRequest.getLocale()))
                .withOwner(new UserDataModel.Builder()
                        .withID(entryVO.getOwner().getId())
                        .withUsername(entryVO.getOwner().getUsername())
                        .build());

        // TODO add tags

        return builder.build();
    }
}
