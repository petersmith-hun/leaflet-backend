package hu.psprog.leaflet.service.exception;

/**
 * Exception to be thrown when a defined contact request blacklist rule is invalid.
 *
 * @author Peter Smith
 */
public class InvalidBlacklistRule extends RuntimeException {

    /**
     * The given rule can be interpreted, but the member count is incorrect.
     *
     * @param memberCount number of members recognized in the rule
     */
    public InvalidBlacklistRule(int memberCount) {
        super(String.format("Invalid number of rule members: expected 3, but was %d", memberCount));
    }

    /**
     * The given rule is completely incomprehensible.
     *
     * @param providedRule provided rule for reference
     */
    public InvalidBlacklistRule(String providedRule) {
        super(String.format("Provided rule is invalid: %s", providedRule));
    }
}
