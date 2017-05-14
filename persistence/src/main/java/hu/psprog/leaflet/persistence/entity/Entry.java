package hu.psprog.leaflet.persistence.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
 *  - {@link Entry} 1:N {@link Comment}
 *  - {@link Entry} N:1 {@link Category}
 *  - {@link Entry} N:1 {@link User}
 *  - {@link Entry} N:M {@link UploadedFile}
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

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_TAGS,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_ENTRY)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_TAG_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_TAG)))
    private List<Tag> tags;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_UPLOADED_FILES,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_UPLOADED_FILES_ENTRY)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_UPLOADED_FILE_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_UPLOADED_FILES_UPLOADED_FILE)))
    private List<UploadedFile> attachments;

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_LINK, unique = true)
    private String link;

    @Column(name = DatabaseConstants.COLUMN_PROLOGUE, columnDefinition = DatabaseConstants.DEF_TEXT)
    private String prologue;

    @Column(name = DatabaseConstants.COLUMN_CONTENT, columnDefinition = DatabaseConstants.DEF_LONGTEXT)
    private String content;

    @Column(name = DatabaseConstants.COLUMN_RAW_CONTENT, columnDefinition = DatabaseConstants.DEF_LONGTEXT)
    private String rawContent;

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
    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    public Entry() {
        // Serializable
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<UploadedFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<UploadedFile> attachments) {
        this.attachments = attachments;
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

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
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

    public EntryStatus getStatus() {
        return status;
    }

    public void setStatus(EntryStatus status) {
        this.status = status;
    }

    /**
     * Builder for {@link Entry} entity.
     */
    public static final class Builder {
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private Long id;
        private User user;
        private Category category;
        private List<Tag> tags;
        private List<UploadedFile> attachments;
        private String title;
        private String link;
        private String prologue;
        private String content;
        private String rawContent;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private Locale locale;
        private EntryStatus status;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
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

        public Builder withAttachments(List<UploadedFile> attachments) {
            this.attachments = attachments;
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

        public Builder withRawContent(String rawContent) {
            this.rawContent = rawContent;
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

        public Entry build() {
            Entry entry = new Entry();
            entry.setCreated(created);
            entry.setLastModified(lastModified);
            entry.setEnabled(enabled);
            entry.setId(id);
            entry.setUser(user);
            entry.setCategory(category);
            entry.setTags(tags);
            entry.setAttachments(attachments);
            entry.setTitle(title);
            entry.setLink(link);
            entry.setPrologue(prologue);
            entry.setContent(content);
            entry.setRawContent(rawContent);
            entry.setSeoTitle(seoTitle);
            entry.setSeoDescription(seoDescription);
            entry.setSeoKeywords(seoKeywords);
            entry.setLocale(locale);
            entry.setStatus(status);
            return entry;
        }
    }
}
