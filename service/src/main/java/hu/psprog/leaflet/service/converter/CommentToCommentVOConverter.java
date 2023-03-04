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

    private static final String DELETED_COMMENT = "DELETED_COMMENT";

    private final UserToUserVOConverter userToUserVOConverter;
    private final EntryToEntryVOConverter entryToEntryVOConverter;

    @Autowired
    public CommentToCommentVOConverter(UserToUserVOConverter userToUserVOConverter, EntryToEntryVOConverter entryToEntryVOConverter) {
        this.userToUserVOConverter = userToUserVOConverter;
        this.entryToEntryVOConverter = entryToEntryVOConverter;
    }

    @Override
    public CommentVO convert(Comment source) {

        return CommentVO.getBuilder()
                .withId(source.getId())
                .withContent(extractContent(source))
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEntryVO(entryToEntryVOConverter.convert(source.getEntry()))
                .withOwner(userToUserVOConverter.convert(source.getUser()))
                .withEnabled(source.isEnabled())
                .withDeleted(source.isDeleted())
                .build();
    }

    private String extractContent(Comment source) {
        return source.isDeleted()
                ? DELETED_COMMENT
                : source.getContent();
    }
}
