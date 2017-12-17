package hu.psprog.leaflet.acceptance.mock;

import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;

/**
 * Mock {@link NotificationService} implementation.
 *
 * @author Peter Smith
 */
public class MockNotificationService implements NotificationService {

    private PasswordResetRequest passwordResetRequest;
    private PasswordResetSuccess passwordResetSuccess;

    @Override
    public void startupFinished(String version) {

    }

    @Override
    public void passwordResetRequested(PasswordResetRequest passwordResetRequest) {
        this.passwordResetRequest = passwordResetRequest;
    }

    @Override
    public void successfulPasswordReset(PasswordResetSuccess passwordResetSuccess) {
        this.passwordResetSuccess = passwordResetSuccess;
    }

    public PasswordResetRequest getPasswordResetRequest() {
        return passwordResetRequest;
    }

    public PasswordResetSuccess getPasswordResetSuccess() {
        return passwordResetSuccess;
    }
}
