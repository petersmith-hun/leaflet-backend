package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;

import java.io.Serializable;

/**
 * @author Peter Smith
 */
public class IdentifiableVO<ID extends Serializable, T extends SerializableEntity> extends BaseVO<T> {

    private ID id;

    public IdentifiableVO() {
        // Serializable
    }

    public IdentifiableVO(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
