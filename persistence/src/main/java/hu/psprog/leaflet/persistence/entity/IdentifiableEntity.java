package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Generic base entity class for entities having ID field.
 *
 * @param <T> {@link Serializable} type of ID field
 * @author Peter Smith
 */
@Data
@MappedSuperclass
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
public abstract class IdentifiableEntity<T extends Serializable> implements SerializableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;
}
