package hu.psprog.leaflet.persistence.entity;

/**
 * User roles.
 *
 * @author Peter Smith
 */
public enum Role {

    /**
     * Default user role for visitors.
     */
    USER,

    /**
     * Role for users signed up via external identity providers.
     */
    EXTERNAL_USER,

    /**
     * Blog editors.
     */
    EDITOR,

    /**
     * Administrators.
     */
    ADMIN,

    /**
     * Virtual users used by external services, like CBFS.
     */
    SERVICE
}
