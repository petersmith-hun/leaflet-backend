package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Tag assignment VO.
 *
 * @author Peter Smith
 */
public class TagAssignmentVO {

    private Long entryID;
    private Long tagID;

    public Long getEntryID() {
        return entryID;
    }

    public Long getTagID() {
        return tagID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TagAssignmentVO)) return false;

        TagAssignmentVO that = (TagAssignmentVO) o;

        return new EqualsBuilder()
                .append(entryID, that.entryID)
                .append(tagID, that.tagID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(entryID)
                .append(tagID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entryID", entryID)
                .append("tagID", tagID)
                .toString();
    }

    public static TagAssignmentVOBuilder getBuilder() {
        return new TagAssignmentVOBuilder();
    }

    /**
     * Builder for {@link TagAssignmentVO}.
     */
    public static final class TagAssignmentVOBuilder {
        private Long entryID;
        private Long tagID;

        private TagAssignmentVOBuilder() {
        }

        public TagAssignmentVOBuilder withEntryID(Long entryID) {
            this.entryID = entryID;
            return this;
        }

        public TagAssignmentVOBuilder withTagID(Long tagID) {
            this.tagID = tagID;
            return this;
        }

        public TagAssignmentVO build() {
            TagAssignmentVO tagAssignmentVO = new TagAssignmentVO();
            tagAssignmentVO.tagID = this.tagID;
            tagAssignmentVO.entryID = this.entryID;
            return tagAssignmentVO;
        }
    }
}
