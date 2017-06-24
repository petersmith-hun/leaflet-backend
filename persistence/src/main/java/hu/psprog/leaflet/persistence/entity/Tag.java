package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Tag entity class.
 *
 * Relations:
 *  - {@link Tag} N:M {@link Entry}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_TAGS)
public class Tag extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_TITLE, unique = true)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, tag.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("id", getId())
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .toString();
    }

    public static TagBuilder getBuilder() {
        return new TagBuilder();
    }

    /**
     * Tag entity builder.
     */
    public static final class TagBuilder {
        private String title;
        private Date created;
        private Long id;
        private Date lastModified;
        private boolean enabled;

        private TagBuilder() {
        }

        public TagBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TagBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public TagBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TagBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public TagBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Tag build() {
            Tag tag = new Tag();
            tag.setTitle(title);
            tag.setCreated(created);
            tag.setId(id);
            tag.setLastModified(lastModified);
            tag.setEnabled(enabled);
            return tag;
        }
    }
}
