package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;

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

    @Override
    public String toString() {
        return (new ReflectionToStringBuilder(this) {

            @Override
            protected boolean accept(Field field) {

                // do NOT include "password" fields in toString
                return super.accept(field) && !field.getName().equals(DatabaseConstants.COLUMN_PASSWORD);
            }
        }).toString();
    }
}
