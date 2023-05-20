package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import hu.psprog.leaflet.web.rest.conversion.entry.EntryVOToEntryDataModelEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CommentVO} value object to {@link ExtendedCommentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToExtendedCommentDataModelConverter implements Converter<CommentVO, ExtendedCommentDataModel> {

    private final EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter;
    private final DateConverter dateConverter;

    @Autowired
    public CommentVOToExtendedCommentDataModelConverter(EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter, DateConverter dateConverter) {
        this.entryVOToEntryDataModelEntityConverter = entryVOToEntryDataModelEntityConverter;
        this.dateConverter = dateConverter;
    }

    @Override
    public ExtendedCommentDataModel convert(CommentVO source) {

        return ExtendedCommentDataModel.getBuilder()
                .withId(source.getId())
                .withOwner(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build())
                .withContent(source.getContent())
                .withDeleted(source.isDeleted())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .withEnabled(source.isEnabled())
                .withAssociatedEntry(entryVOToEntryDataModelEntityConverter.convert(source.getEntryVO()))
                .build();
    }
}
