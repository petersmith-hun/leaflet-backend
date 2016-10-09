package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;

/**
 * @author Peter Smith
 */
public class BaseVO<T extends SerializableEntity> {

    private static final String PASSWORD_FIELD = "password";

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return (new ReflectionToStringBuilder(this) {

            @Override
            protected boolean accept(Field field) {
                return super.accept(field) && !field.getName().equals(PASSWORD_FIELD);
            }

        }).toString();
    }
}
