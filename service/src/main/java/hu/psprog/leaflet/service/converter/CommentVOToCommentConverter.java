package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CommentVO} value object to {@link Comment} entity.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToCommentConverter implements Converter<CommentVO, Comment> {

    @Autowired
    private UserVOToUserConverter userVOToUserConverter;

    @Autowired
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Override
    public Comment convert(CommentVO source) {

        return new Comment.Builder()
                .withId(source.getId())
                .withContent(source.getContent())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEntry(entryVOToEntryConverter.convert(source.getEntryVO()))
                .withUser(userVOToUserConverter.convert(source.getOwner()))
                .createComment();
    }
}
