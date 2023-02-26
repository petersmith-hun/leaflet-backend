package hu.psprog.leaflet.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Dynamic configuration property entity for storing configuration key-value pairs.
 *
 * @author Peter Smith
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_DCP)
public class DynamicConfigurationProperty implements SerializableEntity {

    @Id
    @Column(name = DatabaseConstants.COLUMN_KEY)
    private String key;

    @Column(name = DatabaseConstants.COLUMN_VALUE)
    private String value;
}
