package hu.psprog.leaflet.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * Entity class for uploaded files' meta information.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_UPLOADED_FILES, uniqueConstraints = {
        @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_PATH, name = DatabaseConstants.UK_UPLOADED_FILE_PATH),
        @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_PATH_UUID, name = DatabaseConstants.UK_UPLOADED_FILE_PATH_UUID)
})
public class UploadedFile extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_PATH)
    private String path;

    @Column(name = DatabaseConstants.COLUMN_ORIGINAL_FILENAME)
    private String originalFilename;

    @Column(name = DatabaseConstants.COLUMN_MIME)
    private String mime;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = DatabaseConstants.COLUMN_PATH_UUID)
    private UUID pathUUID;

    @Column(name = DatabaseConstants.COLUMN_STORED_FILENAME)
    private String storedFilename;

    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION)
    private String description;
}
