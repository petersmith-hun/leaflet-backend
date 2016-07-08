package hu.psprog.leaflet.persistence.entity;

import javax.persistence.Entity;

/**
 * Category entity class.
 *
 * @author Peter Smith
 */
@Entity
public class Category extends SelfStatusAwareIdentifiableEntity<Long> {

    private String name;
}
