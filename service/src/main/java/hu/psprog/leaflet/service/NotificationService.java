package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;

/**
 * System notification handler service.
 *
 * @author Peter Smith
 */
public interface NotificationService {

    /**
     * Sends notification after system startup.
     *
     * @param version version of currently running application
     */
    void startupFinished(String version);

    /**
     * Sends response mail for password reset request.
     *
     * @param passwordResetRequest domain object holding required parameters
     */
    void passwordResetRequested(PasswordResetRequest passwordResetRequest);

    /**
     * Sends notification of successful password reset.
     *
     * @param passwordResetSuccess domain object holding required parameters
     */
    void successfulPasswordReset(PasswordResetSuccess passwordResetSuccess);

    /**
     * Sends notification about comment creation.
     *
     * @param commentNotification domain object holding required parameters
     */
    void commentNotification(CommentNotification commentNotification);
}
