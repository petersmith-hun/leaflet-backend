package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Category;

import java.util.Date;

/**
 * VO for {@link Category} entity.
 *
 * @author Peter Smith
 */
public class CategoryVO extends SelfStatusAwareIdentifiableVO<Long, Category> {

    private String title;
    private String description;

    public CategoryVO() {
        // Serializable
    }

    public CategoryVO(Long id, Date created, Date lastModified, boolean enabled, String title, String description) {
        super(id, created, lastModified, enabled);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static CategoryVO wrapMinimumVO(Long id) {
        return new Builder()
                .withId(id)
                .createCategoryVO();
    }

    /**
     * CategoryVO builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;
        private String description;

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

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryVO createCategoryVO() {
            return new CategoryVO(id, created, lastModified, enabled, title, description);
        }
    }
}
