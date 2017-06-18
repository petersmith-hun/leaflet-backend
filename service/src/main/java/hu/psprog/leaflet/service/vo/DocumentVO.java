package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public String getRawContent() {
        return rawContent;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public UserVO getOwner() {
        return owner;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DocumentVO)) return false;

        DocumentVO that = (DocumentVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, that.title)
                .append(link, that.link)
                .append(content, that.content)
                .append(rawContent, that.rawContent)
                .append(seoTitle, that.seoTitle)
                .append(seoDescription, that.seoDescription)
                .append(seoKeywords, that.seoKeywords)
                .append(owner, that.owner)
                .append(locale, that.locale)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .append(link)
                .append(content)
                .append(rawContent)
                .append(seoTitle)
                .append(seoDescription)
                .append(seoKeywords)
                .append(owner)
                .append(locale)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("lastModified", lastModified)
                .append("title", title)
                .append("enabled", enabled)
                .append("link", link)
                .append("content", content)
                .append("rawContent", rawContent)
                .append("seoTitle", seoTitle)
                .append("seoDescription", seoDescription)
                .append("seoKeywords", seoKeywords)
                .append("owner", owner)
                .append("locale", locale)
                .toString();
    }

    public static DocumentVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static DocumentVOBuilder getBuilder() {
        return new DocumentVOBuilder();
    }

    /**
     * Builder for {@link DocumentVO}.
     */
    public static final class DocumentVOBuilder {
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

        private DocumentVOBuilder() {
        }

        public DocumentVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public DocumentVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public DocumentVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public DocumentVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public DocumentVOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public DocumentVOBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public DocumentVOBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public DocumentVOBuilder withRawContent(String rawContent) {
            this.rawContent = rawContent;
            return this;
        }

        public DocumentVOBuilder withSeoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public DocumentVOBuilder withSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public DocumentVOBuilder withSeoKeywords(String seoKeywords) {
            this.seoKeywords = seoKeywords;
            return this;
        }

        public DocumentVOBuilder withOwner(UserVO owner) {
            this.owner = owner;
            return this;
        }

        public DocumentVOBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public DocumentVO build() {
            DocumentVO documentVO = new DocumentVO();
            documentVO.seoTitle = this.seoTitle;
            documentVO.seoDescription = this.seoDescription;
            documentVO.rawContent = this.rawContent;
            documentVO.locale = this.locale;
            documentVO.link = this.link;
            documentVO.content = this.content;
            documentVO.title = this.title;
            documentVO.id = this.id;
            documentVO.lastModified = this.lastModified;
            documentVO.seoKeywords = this.seoKeywords;
            documentVO.enabled = this.enabled;
            documentVO.owner = this.owner;
            documentVO.created = this.created;
            return documentVO;
        }
    }
}
