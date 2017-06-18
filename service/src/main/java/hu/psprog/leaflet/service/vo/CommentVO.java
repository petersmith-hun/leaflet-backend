package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Comment;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * VO for {@link Comment} entity.
 *
 * @author Peter Smith
 */
public class CommentVO extends LogicallyDeletableSelfStatusAwareIdentifiableVO<Long, Comment> {

    public enum OrderBy {
        ID("id"),
        CREATED("created");

        private String field;

        OrderBy(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    private UserVO owner;
    private EntryVO entryVO;
    private String content;

    public UserVO getOwner() {
        return owner;
    }

    public EntryVO getEntryVO() {
        return entryVO;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CommentVO)) return false;

        CommentVO commentVO = (CommentVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(owner, commentVO.owner)
                .append(entryVO, commentVO.entryVO)
                .append(content, commentVO.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(owner)
                .append(entryVO)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("deleted", deleted)
                .append("owner", owner)
                .append("lastModified", lastModified)
                .append("entryVO", entryVO)
                .append("enabled", enabled)
                .append("content", content)
                .toString();
    }

    public static CommentVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static CommentVOBuilder getBuilder() {
        return new CommentVOBuilder();
    }

    /**
     * Builder for {@link CommentVO} class.
     */
    public static final class CommentVOBuilder {
        private Long id;
        private Date created;
        private boolean deleted;
        private Date lastModified;
        private boolean enabled;
        private UserVO owner;
        private EntryVO entryVO;
        private String content;

        private CommentVOBuilder() {
        }

        public CommentVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CommentVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public CommentVOBuilder withDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public CommentVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public CommentVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public CommentVOBuilder withOwner(UserVO owner) {
            this.owner = owner;
            return this;
        }

        public CommentVOBuilder withEntryVO(EntryVO entryVO) {
            this.entryVO = entryVO;
            return this;
        }

        public CommentVOBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public CommentVO build() {
            CommentVO commentVO = new CommentVO();
            commentVO.content = this.content;
            commentVO.entryVO = this.entryVO;
            commentVO.id = this.id;
            commentVO.deleted = this.deleted;
            commentVO.lastModified = this.lastModified;
            commentVO.enabled = this.enabled;
            commentVO.created = this.created;
            commentVO.owner = this.owner;
            return commentVO;
        }
    }
}
