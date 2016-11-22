package hu.psprog.leaflet.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Dynamic configuration property entity for storing configuration key-value pairs.
 *
 * @author Peter Smith
 */
@Entity
@Table(name = DatabaseConstants.TABLE_DCP)
public class DynamicConfigurationProperty implements SerializableEntity {

    @Id
    @Column(name = DatabaseConstants.COLUMN_KEY)
    private String key;

    @Column(name = DatabaseConstants.COLUMN_VALUE)
    private String value;

    public DynamicConfigurationProperty() {
        // Serializable
    }

    public DynamicConfigurationProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}
