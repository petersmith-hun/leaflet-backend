package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

        Comment.CommentBuilder builder = Comment.getBuilder()
                .withId(source.getId())
                .withContent(source.getContent())
                .withCreated(source.getCreated())
                .withLastModified(source.getLastModified())
                .withEnabled(source.isEnabled())
                .withDeleted(source.isDeleted());

        if (Objects.nonNull(source.getEntryVO())) {
            builder.withEntry(Entry.getBuilder()
                    .withId(source.getEntryVO().getId())
                    .build());
        }

        if (Objects.nonNull(source.getOwner())) {
            builder.withUser(User.getBuilder()
                    .withId(source.getOwner().getId())
                    .build());
        }

        return builder.build();
    }
}
