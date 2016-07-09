package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_TAGS,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_TAG_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_TAG)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_ENTRY)))
    private List<Entry> entries;

    public Tag() {
        // Serializable
    }

    public Tag(Long id, Date created, Date lastModified, boolean enabled, String title, List<Entry> entries) {
        super(id, created, lastModified, enabled);
        this.title = title;
        this.entries = entries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
     * Tag entity builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
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

        public Builder withEntries(List<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public Tag createTag() {
            return new Tag(id, created, lastModified, enabled, title, entries);
        }
    }
}
