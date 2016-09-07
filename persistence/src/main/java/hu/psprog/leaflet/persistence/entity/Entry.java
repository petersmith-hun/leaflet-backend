package hu.psprog.leaflet.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Blog entry entity class.
 *
 * Relations:
 *  - {@link Entry} 1:N {@link Attachment}
 *  - {@link Entry} 1:N {@link Comment}
 *  - {@link Entry} N:1 {@link Category}
 *  - {@link Entry} N:1 {@link User}
 *  - {@link Entry} N:M {@link Tag}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_ENTRIES)
public class Entry extends SelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_ENTRY_USER))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DatabaseConstants.COLUMN_CATEGORY_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_ENTRY_CATEGORY))
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_TAGS,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_ENTRY)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_TAG_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_TAG)))
    private List<Tag> tags;

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_LINK, unique = true)
    private String link;

    @Column(name = DatabaseConstants.COLUMN_PROLOGUE, columnDefinition = DatabaseConstants.DEF_TEXT)
    private String prologue;

    @Column(name = DatabaseConstants.COLUMN_CONTENT, columnDefinition = DatabaseConstants.DEF_LONGTEXT)
    private String content;

    @Column(name = DatabaseConstants.COLUMN_SEO_TITLE)
    private String seoTitle;

    @Column(name = DatabaseConstants.COLUMN_SEO_DESCRIPTION)
    private String seoDescription;

    @Column(name = DatabaseConstants.COLUMN_SEO_KEYWORDS)
    private String seoKeywords;

    @Column(name = DatabaseConstants.COLUMN_LOCALE)
    @Enumerated(EnumType.STRING)
    private Locale locale;

    @Column(name = DatabaseConstants.COLUMN_STATUS)
    private EntryStatus status;

    public Entry() {
        // Serializable
    }

    public Entry(Long id, Date created, Date lastModified, boolean enabled, User user, Category category,
                 List<Tag> tags, String title, String link, String prologue, String content, String seoTitle,
                 String seoDescription, String seoKeywords, Locale locale, EntryStatus status) {
        super(id, created, lastModified, enabled);
        this.user = user;
        this.category = category;
        this.tags = tags;
        this.title = title;
        this.link = link;
        this.prologue = prologue;
        this.content = content;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.locale = locale;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrologue() {
        return prologue;
    }

    public void setPrologue(String prologue) {
        this.prologue = prologue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public EntryStatus getStatus() {
        return status;
    }

    public void setStatus(EntryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Entry entity builder.
     */
    public static class Builder {
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private User user;
        private Category category;
        private List<Tag> tags;
        private String title;
        private String link;
        private String prologue;
        private String content;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private Locale locale;
        private EntryStatus status;

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

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder withTags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withPrologue(String prologue) {
            this.prologue = prologue;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withSeoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public Builder withSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public Builder withSeoKeywords(String seoKeywords) {
            this.seoKeywords = seoKeywords;
            return this;
        }

        public Builder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder withStatus(EntryStatus status) {
            this.status = status;
            return this;
        }

        public Entry createEntry() {
            return new Entry(id, created, lastModified, enabled, user, category, tags, title, link, prologue, content,
                    seoTitle, seoDescription, seoKeywords, locale, status);
        }
    }
}
