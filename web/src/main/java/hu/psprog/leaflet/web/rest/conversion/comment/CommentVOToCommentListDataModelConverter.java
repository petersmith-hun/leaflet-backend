package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link CommentVO} value objects to {@link CommentListDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToCommentListDataModelConverter implements Converter<List<CommentVO>, CommentListDataModel> {

    private final CommentVOToCommentDataModelConverter commentVOToCommentDataModelConverter;

    @Autowired
    public CommentVOToCommentListDataModelConverter(CommentVOToCommentDataModelConverter commentVOToCommentDataModelConverter) {
        this.commentVOToCommentDataModelConverter = commentVOToCommentDataModelConverter;
    }

    @Override
    public CommentListDataModel convert(List<CommentVO> source) {

        CommentListDataModel.CommentListDataModelBuilder builder = CommentListDataModel.getBuilder();
        source.stream()
                .map(commentVOToCommentDataModelConverter::convert)
                .forEach(builder::withItem);

        return builder.build();
    }
}
