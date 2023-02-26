package hu.psprog.leaflet.acceptance.mock;

import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.vo.ContactRequestVO;

/**
 * Mock {@link NotificationService} implementation.
 *
 * @author Peter Smith
 */
public class MockNotificationService implements NotificationService {

    private CommentNotification commentNotification;
    private ContactRequestVO contactRequestVO;

    @Override
    public void startupFinished(String version) {
    }

    @Override
    public void commentNotification(CommentNotification commentNotification) {
        this.commentNotification = commentNotification;
    }

    @Override
    public void contactRequestReceived(ContactRequestVO contactRequestVO) {
        this.contactRequestVO = contactRequestVO;
    }

    public CommentNotification getCommentNotification() {
        return commentNotification;
    }

    public ContactRequestVO getContactRequestVO() {
        return contactRequestVO;
    }

    public void reset() {
        commentNotification = null;
        contactRequestVO = null;
    }
}
