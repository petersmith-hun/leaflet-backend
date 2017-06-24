package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Document entity class.
 *
 * Relations:
 *  - {@link Document} N:1 {@link User}
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_DOCUMENTS)
public class Document extends SelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_DOCUMENT_USER))
    private User user;

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_LINK)
    private String link;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(user, document.user)
                .append(title, document.title)
                .append(link, document.link)
                .append(content, document.content)
                .append(rawContent, document.rawContent)
                .append(seoTitle, document.seoTitle)
                .append(seoDescription, document.seoDescription)
                .append(seoKeywords, document.seoKeywords)
                .append(locale, document.locale)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(user)
                .append(title)
                .append(link)
                .append(content)
                .append(rawContent)
                .append(seoTitle)
                .append(seoDescription)
                .append(seoKeywords)
                .append(locale)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("user", user)
                .append("created", getCreated())
                .append("title", title)
                .append("link", link)
                .append("lastModified", getLastModified())
                .append("content", content)
                .append("enabled", isEnabled())
                .append("rawContent", rawContent)
                .append("seoTitle", seoTitle)
                .append("seoDescription", seoDescription)
                .append("seoKeywords", seoKeywords)
                .append("locale", locale)
                .toString();
    }

    public static DocumentBuilder getBuilder() {
        return new DocumentBuilder();
    }

    /**
     * Document entity builder.
     */
    public static final class DocumentBuilder {
        private Date created;
        private Long id;
        private Date lastModified;
        private boolean enabled;
        private User user;
        private String title;
        private String link;
        private String content;
        private String rawContent;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private Locale locale;

        private DocumentBuilder() {
        }

        public DocumentBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public DocumentBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public DocumentBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public DocumentBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DocumentBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public DocumentBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public DocumentBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public DocumentBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public DocumentBuilder withRawContent(String rawContent) {
            this.rawContent = rawContent;
            return this;
        }

        public DocumentBuilder withSeoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public DocumentBuilder withSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public DocumentBuilder withSeoKeywords(String seoKeywords) {
            this.seoKeywords = seoKeywords;
            return this;
        }

        public DocumentBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Document build() {
            Document document = new Document();
            document.setCreated(created);
            document.setId(id);
            document.setLastModified(lastModified);
            document.setEnabled(enabled);
            document.setUser(user);
            document.setTitle(title);
            document.setLink(link);
            document.setContent(content);
            document.setRawContent(rawContent);
            document.setSeoTitle(seoTitle);
            document.setSeoDescription(seoDescription);
            document.setSeoKeywords(seoKeywords);
            document.setLocale(locale);
            return document;
        }
    }
}
