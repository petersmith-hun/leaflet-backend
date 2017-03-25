package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import hu.psprog.leaflet.web.rest.conversion.entry.EntryVOToEntryDataModelEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link CommentVO} value object to {@link ExtendedCommentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToExtendedCommentDataModelConverter implements Converter<CommentVO, ExtendedCommentDataModel> {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter;

    @Override
    public ExtendedCommentDataModel convert(CommentVO source) {
        return new ExtendedCommentDataModel.Builder()
                .withId(source.getId())
                .withOwner(new UserDataModel.Builder()
                        .withID(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build())
                .withContent(source.getContent())
                .withDeleted(source.isDeleted())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .withEnabled(source.isEnabled())
                .withAssociatedEntry(entryVOToEntryDataModelEntityConverter.convert(source.getEntryVO()))
                .build();
    }
}
