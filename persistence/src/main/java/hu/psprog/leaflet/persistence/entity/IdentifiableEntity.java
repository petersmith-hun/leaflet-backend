package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
public abstract class IdentifiableEntity<T extends Serializable> implements SerializableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IdentifiableEntity)) return false;

        IdentifiableEntity<?> that = (IdentifiableEntity<?>) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}
