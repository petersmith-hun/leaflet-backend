package hu.psprog.leaflet.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
