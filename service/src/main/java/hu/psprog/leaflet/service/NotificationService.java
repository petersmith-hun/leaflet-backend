package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;

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

    void successfulPasswordReset();
}
