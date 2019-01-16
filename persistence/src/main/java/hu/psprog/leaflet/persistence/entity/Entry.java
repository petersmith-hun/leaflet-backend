package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
import javax.persistence.UniqueConstraint;
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
@Table(name = DatabaseConstants.TABLE_ENTRIES,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_LINK, name = DatabaseConstants.UK_ENTRY_LINK))
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

    @Column(name = DatabaseConstants.COLUMN_LINK)
    private String link;

    @Column(name = DatabaseConstants.COLUMN_PROLOGUE, columnDefinition = DatabaseConstants.DEF_TEXT)
    private String prologue;

    @Column(name = DatabaseConstants.COLUMN_RAW_CONTENT, columnDefinition = DatabaseConstants.DEF_LONGTEXT)
    private String rawContent;

    @Column(name = DatabaseConstants.COLUMN_SEO_TITLE)
    private String seoTitle;

    @Column(name = DatabaseConstants.COLUMN_SEO_DESCRIPTION, length = 4095)
    private String seoDescription;

    @Column(name = DatabaseConstants.COLUMN_SEO_KEYWORDS)
    private String seoKeywords;

    @Column(name = DatabaseConstants.COLUMN_LOCALE)
    @Enumerated(EnumType.STRING)
    private Locale locale;

    @Column(name = DatabaseConstants.COLUMN_STATUS)
    @Enumerated(EnumType.STRING)
    private EntryStatus status;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(user, entry.user)
                .append(category, entry.category)
                .append(tags, entry.tags)
                .append(attachments, entry.attachments)
                .append(title, entry.title)
                .append(link, entry.link)
                .append(prologue, entry.prologue)
                .append(rawContent, entry.rawContent)
                .append(seoTitle, entry.seoTitle)
                .append(seoDescription, entry.seoDescription)
                .append(seoKeywords, entry.seoKeywords)
                .append(locale, entry.locale)
                .append(status, entry.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(user)
                .append(category)
                .append(tags)
                .append(attachments)
                .append(title)
                .append(link)
                .append(prologue)
                .append(rawContent)
                .append(seoTitle)
                .append(seoDescription)
                .append(seoKeywords)
                .append(locale)
                .append(status)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .append("user", user)
                .append("category", category)
                .append("tags", tags)
                .append("attachments", attachments)
                .append("title", title)
                .append("link", link)
                .append("prologue", prologue)
                .append("rawContent", rawContent)
                .append("seoTitle", seoTitle)
                .append("seoDescription", seoDescription)
                .append("seoKeywords", seoKeywords)
                .append("locale", locale)
                .append("status", status)
                .toString();
    }

    public static EntryBuilder getBuilder() {
        return new EntryBuilder();
    }

    /**
     * Builder for {@link Entry} entity.
     */
    public static final class EntryBuilder {
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
        private String rawContent;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private Locale locale;
        private EntryStatus status;

        private EntryBuilder() {
        }

        public EntryBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public EntryBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public EntryBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public EntryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EntryBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public EntryBuilder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public EntryBuilder withTags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public EntryBuilder withAttachments(List<UploadedFile> attachments) {
            this.attachments = attachments;
            return this;
        }

        public EntryBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public EntryBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public EntryBuilder withPrologue(String prologue) {
            this.prologue = prologue;
            return this;
        }

        public EntryBuilder withRawContent(String rawContent) {
            this.rawContent = rawContent;
            return this;
        }

        public EntryBuilder withSeoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public EntryBuilder withSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public EntryBuilder withSeoKeywords(String seoKeywords) {
            this.seoKeywords = seoKeywords;
            return this;
        }

        public EntryBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public EntryBuilder withStatus(EntryStatus status) {
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
