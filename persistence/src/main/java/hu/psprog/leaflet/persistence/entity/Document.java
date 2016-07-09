package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
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

    @Column(name = DatabaseConstants.COLUMN_SEO_TITLE)
    private String seoTitle;

    @Column(name = DatabaseConstants.COLUMN_SEO_DESCRIPTION)
    private String seoDescription;

    @Column(name = DatabaseConstants.COLUMN_SEO_KEYWORDS)
    private String seoKeywords;

    @Column(name = DatabaseConstants.COLUMN_LOCALE)
    @Enumerated(EnumType.STRING)
    private Locale locale;

    public Document() {
        // Serializable
    }

    public Document(Long id, Date created, Date lastModified, boolean enabled, User user, String title, String link,
                    String content, String seoTitle, String seoDescription, String seoKeywords, Locale locale) {
        super(id, created, lastModified, enabled);
        this.user = user;
        this.title = title;
        this.link = link;
        this.content = content;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.locale = locale;
    }

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
    public String toString() {
        return super.toString();
    }

    /**
     * Document entity builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private User user;
        private String title;
        private String link;
        private String content;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private Locale locale;

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

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
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

        public Document createDocument() {
            return new Document(id, created, lastModified, enabled, user, title, link, content,
                    seoTitle, seoDescription, seoKeywords, locale);
        }
    }
}
