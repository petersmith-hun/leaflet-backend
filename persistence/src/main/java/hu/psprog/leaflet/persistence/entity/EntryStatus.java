package hu.psprog.leaflet.persistence.entity;

/**
 * Entry statuses.
 *
 * @author Peter Smith
 */
public enum EntryStatus {

    /**
     * Created entry is a draft.
     */
    DRAFT,

    /**
     * Created entry is finished, but under review by a moderator.
     */
    REVIEW,

    /**
     * Created entry is finalized and public for visitors.
     */
    PUBLIC;
}
