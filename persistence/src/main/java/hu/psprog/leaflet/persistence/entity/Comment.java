package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Comment entity class.
 *
 * Relations:
 *  - {@link Comment} N:1 {@link Entry}
 *  - {@link Comment} N:1 {@link User}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_COMMENTS)
public class Comment extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_COMMENT_USER))
    private User user;

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_COMMENT_ENTRY))
    private Entry entry;

    @Column(name = DatabaseConstants.COLUMN_CONTENT, length = 2000)
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(user, comment.user)
                .append(entry, comment.entry)
                .append(content, comment.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(user)
                .append(entry)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("user", user)
                .append("created", getCreated())
                .append("deleted", isDeleted())
                .append("entry", entry)
                .append("lastModified", getLastModified())
                .append("content", content)
                .append("enabled", isEnabled())
                .toString();
    }

    public static CommentBuilder getBuilder() {
        return new CommentBuilder();
    }

    /**
     * Comment entity builder.
     */
    public static final class CommentBuilder {
        private boolean deleted;
        private Date created;
        private Long id;
        private Date lastModified;
        private boolean enabled;
        private User user;
        private Entry entry;
        private String content;

        private CommentBuilder() {
        }

        public CommentBuilder withDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public CommentBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public CommentBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CommentBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public CommentBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public CommentBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public CommentBuilder withEntry(Entry entry) {
            this.entry = entry;
            return this;
        }

        public CommentBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.setDeleted(deleted);
            comment.setCreated(created);
            comment.setId(id);
            comment.setLastModified(lastModified);
            comment.setEnabled(enabled);
            comment.setUser(user);
            comment.setEntry(entry);
            comment.setContent(content);
            return comment;
        }
    }
}
