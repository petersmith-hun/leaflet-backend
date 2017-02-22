package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.Locale;

import java.util.Date;

/**
 * VO for {@link Document} entity.
 *
 * @author Peter Smith
 */
public class DocumentVO extends SelfStatusAwareIdentifiableVO<Long, Document> {

    public enum OrderBy {
        ID("id"),
        CREATED("created");

        private String field;

        OrderBy(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    private String title;
    private String link;
    private String content;
    private String rawContent;
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private UserVO owner;
    private Locale locale;

    public DocumentVO() {
        // Serializable
    }

    public DocumentVO(Long id, Date created, Date lastModified, boolean enabled, String title, String link,
                      String content, String rawContent, String seoTitle, String seoDescription, String seoKeywords,
                      UserVO owner, Locale locale) {
        super(id, created, lastModified, enabled);
        this.title = title;
        this.link = link;
        this.content = content;
        this.rawContent = rawContent;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.owner = owner;
        this.locale = locale;
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

    public UserVO getOwner() {
        return owner;
    }

    public void setOwner(UserVO owner) {
        this.owner = owner;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public static DocumentVO wrapMinimumVO(Long id) {
        return new Builder()
                .withId(id)
                .createDocumentVO();
    }

    /**
     * Builder for {@link DocumentVO}.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
        private String link;
        private String content;
        private String rawContent;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private UserVO owner;
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

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
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

        public Builder withOwner(UserVO owner) {
            this.owner = owner;
            return this;
        }

        public Builder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public DocumentVO createDocumentVO() {
            return new DocumentVO(id, created, lastModified, enabled, title, link, content, rawContent,
                    seoTitle, seoDescription, seoKeywords, owner, locale);
        }
    }
}
