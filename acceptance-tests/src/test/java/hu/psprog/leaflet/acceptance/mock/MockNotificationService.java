package hu.psprog.leaflet.acceptance.mock;

import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.vo.ContactRequestVO;

/**
 * Mock {@link NotificationService} implementation.
 *
 * @author Peter Smith
 */
public class MockNotificationService implements NotificationService {

    private PasswordResetRequest passwordResetRequest;
    private PasswordResetSuccess passwordResetSuccess;
    private CommentNotification commentNotification;
    private ContactRequestVO contactRequestVO;

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

    @Override
    public void commentNotification(CommentNotification commentNotification) {
        this.commentNotification = commentNotification;
    }

    @Override
    public void contactRequestReceived(ContactRequestVO contactRequestVO) {
        this.contactRequestVO = contactRequestVO;
    }

    public PasswordResetRequest getPasswordResetRequest() {
        return passwordResetRequest;
    }

    public PasswordResetSuccess getPasswordResetSuccess() {
        return passwordResetSuccess;
    }

    public CommentNotification getCommentNotification() {
        return commentNotification;
    }

    public ContactRequestVO getContactRequestVO() {
        return contactRequestVO;
    }

    public void reset() {
        passwordResetSuccess = null;
        passwordResetRequest = null;
        commentNotification = null;
        contactRequestVO = null;
    }
}
