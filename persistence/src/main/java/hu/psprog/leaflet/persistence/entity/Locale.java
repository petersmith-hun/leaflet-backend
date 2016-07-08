package hu.psprog.leaflet.persistence.entity;

/**
 * Locales with language codes.
 *
 * @author Peter Smith
 */
public enum Locale {

    /**
     * Hungarian.
     */
    HU("hu_HU"),

    /**
     * English (United States).
     */
    EN("en_US");

    private String code;

    Locale(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
