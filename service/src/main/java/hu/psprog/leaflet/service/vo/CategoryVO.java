package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Category;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * VO for {@link Category} entity.
 *
 * @author Peter Smith
 */
public class CategoryVO extends SelfStatusAwareIdentifiableVO<Long, Category> {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CategoryVO)) return false;

        CategoryVO that = (CategoryVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, that.title)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("description", description)
                .append("id", id)
                .append("created", created)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .toString();
    }

    public static CategoryVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static CategoryVOBuilder getBuilder() {
        return new CategoryVOBuilder();
    }

    /**
     * Builder for {@link CategoryVO}.
     */
    public static final class CategoryVOBuilder {
        private String title;
        private String description;
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;

        private CategoryVOBuilder() {
        }

        public CategoryVOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public CategoryVOBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public CategoryVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public CategoryVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public CategoryVO build() {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.id = this.id;
            categoryVO.title = this.title;
            categoryVO.lastModified = this.lastModified;
            categoryVO.description = this.description;
            categoryVO.enabled = this.enabled;
            categoryVO.created = this.created;
            return categoryVO;
        }
    }
}
