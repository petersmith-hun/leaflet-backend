package hu.psprog.leaflet.persistence.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Blog entry entity class.
 *
 * @author Peter Smith
 */
@Entity
@Table(name = "blog_entries")
public class Entry extends SelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "FK_BLOG_ENTRY_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_category_id",
            foreignKey = @ForeignKey(name = "FK_BLOG_ENTRY_BLOG_CATEGORY"))
    private Category category;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "prologue", columnDefinition = "TEXT")
    private String prologue;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "seo_title")
    private String seoTitle;

    @Column(name = "seo_description")
    private String seoDescription;

    @Column(name = "seo_keywords")
    private String seoKeywords;

    @Column(name = "locale")
    @Enumerated(EnumType.STRING)
    private Locale locale;

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

    @Override
    public String toString() {
        return "Entry{" +
                "user=" + user +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", prologue='" + prologue + '\'' +
                ", content='" + content + '\'' +
                ", seoTitle='" + seoTitle + '\'' +
                ", seoDescription='" + seoDescription + '\'' +
                ", seoKeywords='" + seoKeywords + '\'' +
                ", locale=" + locale +
                '}';
    }

    /**
     * Entry entity builder.
     */
    public static class Builder {
        private Entry entry;

        public Builder() {
            this.entry = new Entry();
        }

        public Builder withID(Long id) {
            this.entry.setId(id);

            return this;
        }

        public Builder withUser(User user) {
            this.entry.setUser(user);

            return this;
        }

        public Builder withCategory(Category category) {
            this.entry.setCategory(category);

            return this;
        }

        public Builder withTitle(String title) {
            this.entry.setTitle(title);

            return this;
        }

        public Builder withLink(String link) {
            this.entry.setLink(link);

            return this;
        }

        public Builder withContent(String content) {
            this.entry.setContent(content);

            return this;
        }

        public Builder withPrologue(String prologue) {
            this.entry.setPrologue(prologue);

            return this;
        }

        public Builder withSeoTitle(String seoTitle) {
            this.entry.setSeoTitle(seoTitle);

            return this;
        }

        public Builder withSeoDescription(String seoDescription) {
            this.entry.setSeoDescription(seoDescription);

            return this;
        }

        public Builder withSeoKeywords(String seoKeywords) {
            this.entry.setSeoKeywords(seoKeywords);

            return this;
        }

        public Builder withCreationDate(Date created) {
            this.entry.setCreated(created);

            return this;
        }

        public Builder withLastModificationDate(Date modified) {
            this.entry.setLastModified(modified);

            return this;
        }

        public Builder isEnabled(boolean enabled) {
            this.entry.setEnabled(enabled);

            return this;
        }

        public Builder withLocale(Locale locale) {
            this.entry.setLocale(locale);

            return this;
        }

        public Entry build() {
            return this.entry;
        }
    }
}
