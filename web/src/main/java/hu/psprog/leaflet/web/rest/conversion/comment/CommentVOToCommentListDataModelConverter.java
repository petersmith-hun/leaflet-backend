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

    @Autowired
    private CommentVOToCommentDataModelConverter commentVOToCommentDataModelConverter;

    @Override
    public CommentListDataModel convert(List<CommentVO> source) {

        CommentListDataModel.Builder builder = new CommentListDataModel.Builder();
        source.forEach(commentVO -> builder.withItem(commentVOToCommentDataModelConverter.convert(commentVO)));

        return builder.build();
    }
}
