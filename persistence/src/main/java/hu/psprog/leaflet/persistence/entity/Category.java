package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = DatabaseConstants.MAPPED_BY_CATEGORY)
    private List<Entry> entries;

    public Category() {
        // Serializable
    }

    public Category(Long id, Date created, Date lastModified, boolean enabled, String title, String description,
                    List<Entry> entries) {
        super(id, created, lastModified, enabled);
        this.title = title;
        this.description = description;
        this.entries = entries;
    }

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

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Category entity builder
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
        private String description;
        private List<Entry> entries;

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

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withEntries(List<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public Category createCategory() {
            return new Category(id, created, lastModified, enabled, title, description, entries);
        }
    }
}
