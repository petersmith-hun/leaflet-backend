package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Comment;

import java.util.Date;

/**
 * VO for {@link Comment} entity.
 *
 * @author Peter Smith
 */
public class CommentVO extends SelfStatusAwareIdentifiableVO<Long, Comment> {

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

    private UserVO owner;
    private EntryVO entryVO;
    private String content;

    public CommentVO() {
        // Serializable
    }

    public CommentVO(Long id, Date created, Date lastModified, boolean enabled, UserVO owner, EntryVO entryVO, String content) {
        super(id, created, lastModified, enabled);
        this.owner = owner;
        this.entryVO = entryVO;
        this.content = content;
    }

    public UserVO getOwner() {
        return owner;
    }

    public void setOwner(UserVO owner) {
        this.owner = owner;
    }

    public EntryVO getEntryVO() {
        return entryVO;
    }

    public void setEntryVO(EntryVO entryVO) {
        this.entryVO = entryVO;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Builder for {@link CommentVO} class.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private UserVO owner;
        private EntryVO entryVO;
        private String content;

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

        public Builder withOwner(UserVO owner) {
            this.owner = owner;
            return this;
        }

        public Builder withEntryVO(EntryVO entryVO) {
            this.entryVO = entryVO;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public CommentVO createCommentVO() {
            return new CommentVO(id, created, lastModified, enabled, owner, entryVO, content);
        }
    }
}
