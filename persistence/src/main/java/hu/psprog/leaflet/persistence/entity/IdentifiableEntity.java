package hu.psprog.leaflet.persistence.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Generic base entity class for entities having ID field.
 *
 * @param <T> {@link Serializable} type of ID field
 * @author Peter Smith
 */
@MappedSuperclass
public class IdentifiableEntity<T extends Serializable> implements SerializableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private T id;

    public IdentifiableEntity() {
        // Serializable
    }

    public IdentifiableEntity(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
