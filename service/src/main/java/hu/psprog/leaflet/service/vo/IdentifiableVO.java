package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Value objects having identifier field (ID).
 *
 * @author Peter Smith
 */
public abstract class IdentifiableVO<ID extends Serializable, T extends SerializableEntity> extends BaseVO<T> {

    protected ID id;

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IdentifiableVO)) return false;

        IdentifiableVO<?, ?> that = (IdentifiableVO<?, ?>) o;

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
