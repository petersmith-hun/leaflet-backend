package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Locale;

import java.util.Date;

/**
 * @author Peter Smith
 */
public class EntryVO extends SelfStatusAwareIdentifiableVO<Long, Entry> {

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
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private String entryStatus;
    private UserVO owner;
    private Locale locale;

    public EntryVO() {
        // Serializable
    }

    public EntryVO(Long id, Date created, Date lastModified, boolean enabled, String title, String link,
                   String prologue, String content, String seoTitle, String seoDescription, String seoKeywords,
                   String entryStatus, UserVO owner, Locale locale) {
        super(id, created, lastModified, enabled);
        this.title = title;
        this.link = link;
        this.prologue = prologue;
        this.content = content;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.seoKeywords = seoKeywords;
        this.entryStatus = entryStatus;
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

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
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

    /**
     * EntryVO builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
        private String link;
        private String prologue;
        private String content;
        private String seoTitle;
        private String seoDescription;
        private String seoKeywords;
        private String entryStatus;
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

        public Builder withEntryStatus(String entryStatus) {
            this.entryStatus = entryStatus;
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

        public EntryVO createEntryVO() {
            return new EntryVO(id, created, lastModified, enabled, title, link, prologue, content,
                    seoTitle, seoDescription, seoKeywords, entryStatus, owner, locale);
        }
    }
}
