package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Tag;

import java.util.Date;

/**
 * VO for {@link Tag} entity.
 *
 * @author Peter Smith
 */
public class TagVO extends SelfStatusAwareIdentifiableVO<Long, Tag> {

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

    public TagVO() {
        // Serializable
    }

    public TagVO(Long id, Date created, Date lastModified, boolean enabled, String title) {
        super(id, created, lastModified, enabled);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Builder for {@link TagVO}.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;

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

        public TagVO createTagVO() {
            return new TagVO(id, created, lastModified, enabled, title);
        }
    }
}
