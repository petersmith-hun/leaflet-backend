package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CommentVO} value object to {@link CommentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToCommentDataModelConverter implements Converter<CommentVO, CommentDataModel> {

    private final DateConverter dateConverter;

    @Autowired
    public CommentVOToCommentDataModelConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public CommentDataModel convert(CommentVO source) {

        return CommentDataModel.getBuilder()
                .withId(source.getId())
                .withOwner(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build())
                .withContent(source.getContent())
                .withDeleted(source.isDeleted())
                .withEnabled(source.isEnabled())
                .withCreated(dateConverter.convert(source.getCreated()))
                .withLastModified(dateConverter.convert(source.getLastModified()))
                .build();
    }
}
