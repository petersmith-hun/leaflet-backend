package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts a {@link List} of {@link CommentVO} objects to {@link ExtendedCommentListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToExtendedCommentListDataModelConverter implements Converter<List<CommentVO>, ExtendedCommentListDataModel> {

    private final CommentVOToExtendedCommentDataModelConverter commentVOToExtendedCommentDataModelConverter;

    @Autowired
    public CommentVOToExtendedCommentListDataModelConverter(CommentVOToExtendedCommentDataModelConverter commentVOToExtendedCommentDataModelConverter) {
        this.commentVOToExtendedCommentDataModelConverter = commentVOToExtendedCommentDataModelConverter;
    }

    @Override
    public ExtendedCommentListDataModel convert(List<CommentVO> source) {

        ExtendedCommentListDataModel.ExtendedCommentListDataModelBuilder builder = ExtendedCommentListDataModel.getBuilder();
        source.stream()
                .map(commentVOToExtendedCommentDataModelConverter::convert)
                .forEach(builder::withItem);

        return builder.build();
    }
}
