package hu.psprog.leaflet.persistence.entity;

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

    public Comment() {
        // Serializable
    }

    public Comment(Long id, Date created, Date lastModified, boolean enabled, boolean deleted, User user, Entry entry, String content) {
        super(id, created, lastModified, enabled, deleted);
        this.user = user;
        this.entry = entry;
        this.content = content;
    }

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
    public String toString() {
        return super.toString();
    }

    /**
     * Comment entity builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private boolean deleted;
        private User user;
        private Entry entry;
        private String content;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder isDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withEntry(Entry entry) {
            this.entry = entry;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Comment createComment() {
            return new Comment(id, created, lastModified, enabled, deleted, user, entry, content);
        }
    }
}
