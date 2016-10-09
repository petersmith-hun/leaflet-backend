package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Attachment;

import java.util.Date;

/**
 * VO for {@link Attachment} entity.
 *
 * @author Peter Smith
 */
public class AttachmentVO extends SelfStatusAwareIdentifiableVO<Long, Attachment> {

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

    private EntryVO entryVO;
    private String title;
    private String filename;
    private String description;
    private String type;
    private boolean isProtected;

    public AttachmentVO() {
        // Serializable
    }

    public AttachmentVO(Long id, Date created, Date lastModified, boolean enabled, EntryVO entryVO, String title,
                        String filename, String description, String type, boolean isProtected) {
        super(id, created, lastModified, enabled);
        this.entryVO = entryVO;
        this.title = title;
        this.filename = filename;
        this.description = description;
        this.type = type;
        this.isProtected = isProtected;
    }

    public EntryVO getEntryVO() {
        return entryVO;
    }

    public void setEntryVO(EntryVO entryVO) {
        this.entryVO = entryVO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsProtected() {
        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    /**
     * Attachment value object builder.
     */
    public static class Builder {

        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private EntryVO entryVO;
        private String title;
        private String filename;
        private String description;
        private String type;
        private boolean isProtected;

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

        public Builder withEntryVO(EntryVO entryVO) {
            this.entryVO = entryVO;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withIsProtected(boolean isProtected) {
            this.isProtected = isProtected;
            return this;
        }

        public AttachmentVO createAttachmentVO() {
            return new AttachmentVO(id, created, lastModified, enabled, entryVO, title, filename, description,
                    type, isProtected);
        }
    }
}
