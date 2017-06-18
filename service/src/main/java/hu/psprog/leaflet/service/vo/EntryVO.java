package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

/**
 * Value object for {@link Entry} entity.
 *
 * @author Peter Smith
 */
public class EntryVO extends SelfStatusAwareIdentifiableVO<Long, Entry> implements CustomSEODataProviderVO<EntryVO> {

    public enum OrderBy {
        ID("id"),
        TITLE("title"),
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
    private String prologue;
    private String content;
    private String rawContent;
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private String entryStatus;
    private UserVO owner;
    private CategoryVO category;
    private Locale locale;
    private List<UploadedFileVO> attachments;
    private List<TagVO> tags;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPrologue() {
        return prologue;
    }

    public String getContent() {
        return content;
    }

    public String getRawContent() {
        return rawContent;
    }

    @Override
    public String getSeoTitle() {
        return seoTitle;
    }

    @Override
    public String getSeoDescription() {
        return seoDescription;
    }

    @Override
    public String getSeoKeywords() {
        return seoKeywords;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    public UserVO getOwner() {
        return owner;
    }

    public CategoryVO getCategory() {
        return category;
    }

    public Locale getLocale() {
        return locale;
    }

    public List<UploadedFileVO> getAttachments() {
        return attachments;
    }

    public List<TagVO> getTags() {
        return tags;
    }

    @Override
    public EntryVO getEntity() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EntryVO)) return false;

        EntryVO entryVO = (EntryVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, entryVO.title)
                .append(link, entryVO.link)
                .append(prologue, entryVO.prologue)
                .append(content, entryVO.content)
                .append(rawContent, entryVO.rawContent)
                .append(seoTitle, entryVO.seoTitle)
                .append(seoDescription, entryVO.seoDescription)
                .append(seoKeywords, entryVO.seoKeywords)
                .append(entryStatus, entryVO.entryStatus)
                .append(owner, entryVO.owner)
                .append(category, entryVO.category)
                .append(locale, entryVO.locale)
                .append(attachments, entryVO.attachments)
                .append(tags, entryVO.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .append(link)
                .append(prologue)
                .append(content)
                .append(rawContent)
                .append(seoTitle)
                .append(seoDescription)
                .append(seoKeywords)
                .append(entryStatus)
                .append(owner)
                .append(category)
                .append(locale)
                .append(attachments)
                .append(tags)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .append("title", title)
                .append("link", link)
                .append("prologue", prologue)
                .append("content", content)
                .append("rawContent", rawContent)
                .append("seoTitle", seoTitle)
                .append("seoDescription", seoDescription)
                .append("seoKeywords", seoKeywords)
                .append("entryStatus", entryStatus)
                .append("owner", owner)
                .append("category", category)
                .append("locale", locale)
                .append("attachments", attachments)
                .append("tags", tags)
                .toString();
    }

    public static EntryVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static EntryVOBuilder getBuilder() {
        return new EntryVOBuilder();
    }

    /**
     * EntryVO builder.
     */
    public static final class EntryVOBuilder {
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
        private String link;
        private String prologue;
        private String content;
        private String rawContent;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private String entryStatus;
        private UserVO owner;
        private CategoryVO category;
        private Locale locale;
        private List<UploadedFileVO> attachments;
        private List<TagVO> tags;

        private EntryVOBuilder() {
        }

        public EntryVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EntryVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public EntryVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public EntryVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public EntryVOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public EntryVOBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public EntryVOBuilder withPrologue(String prologue) {
            this.prologue = prologue;
            return this;
        }

        public EntryVOBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public EntryVOBuilder withRawContent(String rawContent) {
            this.rawContent = rawContent;
            return this;
        }

        public EntryVOBuilder withSeoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public EntryVOBuilder withSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public EntryVOBuilder withSeoKeywords(String seoKeywords) {
            this.seoKeywords = seoKeywords;
            return this;
        }

        public EntryVOBuilder withEntryStatus(String entryStatus) {
            this.entryStatus = entryStatus;
            return this;
        }

        public EntryVOBuilder withOwner(UserVO owner) {
            this.owner = owner;
            return this;
        }

        public EntryVOBuilder withCategory(CategoryVO category) {
            this.category = category;
            return this;
        }

        public EntryVOBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public EntryVOBuilder withAttachments(List<UploadedFileVO> attachments) {
            this.attachments = attachments;
            return this;
        }

        public EntryVOBuilder withTags(List<TagVO> tags) {
            this.tags = tags;
            return this;
        }

        public EntryVO build() {
            EntryVO entryVO = new EntryVO();
            entryVO.seoDescription = this.seoDescription;
            entryVO.locale = this.locale;
            entryVO.tags = this.tags;
            entryVO.title = this.title;
            entryVO.seoKeywords = this.seoKeywords;
            entryVO.content = this.content;
            entryVO.category = this.category;
            entryVO.link = this.link;
            entryVO.prologue = this.prologue;
            entryVO.id = this.id;
            entryVO.lastModified = this.lastModified;
            entryVO.enabled = this.enabled;
            entryVO.entryStatus = this.entryStatus;
            entryVO.created = this.created;
            entryVO.seoTitle = this.seoTitle;
            entryVO.rawContent = this.rawContent;
            entryVO.owner = this.owner;
            entryVO.attachments = this.attachments;
            return entryVO;
        }
    }
}
