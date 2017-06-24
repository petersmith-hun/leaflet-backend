package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Category entity class.
 *
 * Relations:
 *  - {@link Category} 1:N {@link Entry}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_CATEGORIES)
public class Category extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION)
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, category.title)
                .append(description, category.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("description", description)
                .append("id", getId())
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .toString();
    }

    public static CategoryBuilder getBuilder() {
        return new CategoryBuilder();
    }

    /**
     * Builder for {@link Category}.
     */
    public static final class CategoryBuilder {
        private String title;
        private Date created;
        private String description;
        private Long id;
        private Date lastModified;
        private boolean enabled;

        private CategoryBuilder() {
        }

        public CategoryBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public CategoryBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public CategoryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public CategoryBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setTitle(title);
            category.setCreated(created);
            category.setDescription(description);
            category.setId(id);
            category.setLastModified(lastModified);
            category.setEnabled(enabled);
            return category;
        }
    }
}
