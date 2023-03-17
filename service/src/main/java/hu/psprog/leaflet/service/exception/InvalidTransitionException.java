package hu.psprog.leaflet.service.exception;

import hu.psprog.leaflet.persistence.entity.EntryStatus;

/**
 * Exception to be thrown when an entry publication status transition do not conform the valid transition rules.
 *
 * @author Peter Smith
 */
public class InvalidTransitionException extends ServiceException {

    private static final String MESSAGE_PATTERN = "Invalid publication status transition for entry of ID [%s]: [%s] -> [%s]";

    public InvalidTransitionException(Long entryID, EntryStatus currentStatus, EntryStatus newStatus) {
        super(String.format(MESSAGE_PATTERN, entryID, currentStatus, newStatus));
    }
}
