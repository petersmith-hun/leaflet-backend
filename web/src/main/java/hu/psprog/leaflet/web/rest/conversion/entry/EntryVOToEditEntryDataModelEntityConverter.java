package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import hu.psprog.leaflet.web.rest.conversion.file.UploadedFileVOToFileDataModelConverter;
import hu.psprog.leaflet.web.rest.conversion.tag.TagVOToTagDataModelEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Converts {@link EntryVO} value object to {@link EditEntryDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEditEntryDataModelEntityConverter implements Converter<EntryVO, EditEntryDataModel> {

    private final UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter;
    private final TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter;
    private final DateConverter dateConverter;

    @Autowired
    public EntryVOToEditEntryDataModelEntityConverter(UploadedFileVOToFileDataModelConverter uploadedFileVOToFileDataModelConverter,
                                                      TagVOToTagDataModelEntityConverter tagVOToTagDataModelEntityConverter, DateConverter dateConverter) {
        this.uploadedFileVOToFileDataModelConverter = uploadedFileVOToFileDataModelConverter;
        this.tagVOToTagDataModelEntityConverter = tagVOToTagDataModelEntityConverter;
        this.dateConverter = dateConverter;
    }

    @Override
    public EditEntryDataModel convert(EntryVO entryVO) {

        EditEntryDataModel.EditEntryDataModelBuilder builder = EditEntryDataModel.getBuilder()
                .withRawContent(entryVO.getRawContent())
                .withEnabled(entryVO.isEnabled())
                .withEntryStatus(entryVO.getEntryStatus())
                .withLastModified(dateConverter.convert(entryVO.getLastModified()))
                .withTitle(entryVO.getTitle())
                .withLink(entryVO.getLink())
                .withPrologue(entryVO.getPrologue())
                .withCreated(dateConverter.convert(entryVO.getCreated()))
                .withPublished(dateConverter.convert(entryVO.getPublished()))
                .withId(entryVO.getId())
                .withLocale(entryVO.getLocale().name())
                .withCategory(CategoryDataModel.getBuilder()
                        .withTitle(entryVO.getCategory().getTitle())
                        .withId(entryVO.getCategory().getId())
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
