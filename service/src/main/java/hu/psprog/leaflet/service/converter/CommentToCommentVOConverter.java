package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class CommentToCommentVOConverter implements Converter<Comment, CommentVO> {

    @Override
    public CommentVO convert(Comment source) {

        return null;
    }
}
