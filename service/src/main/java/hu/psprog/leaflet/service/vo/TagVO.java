package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Tag;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TagVO)) return false;

        TagVO tagVO = (TagVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(title, tagVO.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("title", title)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .toString();
    }

    public static TagVO wrapMinimumVO(Long id) {
        return getBuilder()
                .withId(id)
                .build();
    }

    public static TagVOBuilder getBuilder() {
        return new TagVOBuilder();
    }

    /**
     * Builder for {@link TagVO}.
     */
    public static final class TagVOBuilder {
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String title;

        private TagVOBuilder() {
        }

        public TagVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TagVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public TagVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public TagVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public TagVOBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public TagVO build() {
            TagVO tagVO = new TagVO();
            tagVO.id = this.id;
            tagVO.lastModified = this.lastModified;
            tagVO.title = this.title;
            tagVO.enabled = this.enabled;
            tagVO.created = this.created;
            return tagVO;
        }
    }
}
