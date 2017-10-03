package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import hu.psprog.leaflet.web.rest.conversion.file.UploadedFileVOToFileDataModelConverter;
import hu.psprog.leaflet.web.rest.conversion.tag.TagVOToTagDataModelEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Converts {@link EntryVO} value object to {@link EditEntryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEditEntryDataModelEntityConverter implements Converter<EntryVO, EditEntryDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;
    private UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter;
    private TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter;

    @Autowired
    public EntryVOToEditEntryDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest,
                                                      UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter,
                                                      TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
        this.uploadedFileVOToFileDataModelConverter = uploadedFileVOToFileDataModelConverter;
        this.tagVOToTagDataModelEntityConverter = tagVOToTagDataModelEntityConverter;
    }

    @Override
    public EditEntryDataModel convert(EntryVO entryVO) {

        EditEntryDataModel.EditEntryDataModelBuilder builder = EditEntryDataModel.getExtendedBuilder()
                .withRawContent(entryVO.getRawContent())
                .withEnabled(entryVO.isEnabled())
                .withEntryStatus(entryVO.getEntryStatus())
                .withLastModified(commonFormatter.formatDate(entryVO.getLastModified(), httpServletRequest.getLocale()))
                .withTitle(entryVO.getTitle())
                .withLink(entryVO.getLink())
                .withPrologue(entryVO.getPrologue())
                .withCreated(commonFormatter.formatDate(entryVO.getCreated(), httpServletRequest.getLocale()))
                .withId(entryVO.getId())
                .withLocale(entryVO.getLocale().name())
                .withCategory(CategoryDataModel.getBuilder()
                        .withTitle(entryVO.getCategory().getTitle())
                        .withID(entryVO.getCategory().getId())
                        .build())
                .withUser(UserDataModel.getBuilder()
                        .withId(entryVO.getOwner().getId())
                        .withUsername(entryVO.getOwner().getUsername())
                        .build())
                .withAttachments(entryVO.getAttachments().stream()
                        .map(uploadedFileVOToFileDataModelConverter::convert)
                        .collect(Collectors.toList()))
                .withTags(entryVO.getTags().stream()
                        .map(tagVOToTagDataModelEntityConverter::convert)
                        .collect(Collectors.toList()));

        return builder.build();
    }
}
