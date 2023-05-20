package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Tag entity class.
 *
 * Relations:
 *  - {@link Tag} N:M {@link Entry}
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_TAGS,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_TITLE, name = DatabaseConstants.UK_TAG_TITLE))
public class Tag extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;
}
