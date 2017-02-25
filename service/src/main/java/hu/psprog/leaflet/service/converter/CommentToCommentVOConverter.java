package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Comment} to {@link CommentVO} object.
 *
 * @author Peter Smith
 */
@Component
public class CommentToCommentVOConverter implements Converter<Comment, CommentVO> {

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    @Autowired
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @Override
    public CommentVO convert(Comment source) {

        return new CommentVO.Builder()
                .withId(source.getId())
                .withContent(source.getContent())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEntryVO(entryToEntryVOConverter.convert(source.getEntry()))
                .withOwner(userToUserVOConverter.convert(source.getUser()))
                .withEnabled(source.isEnabled())
                .withDeleted(source.isDeleted())
                .createCommentVO();
    }
}
