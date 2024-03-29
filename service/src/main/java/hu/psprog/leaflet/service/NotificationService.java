package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.vo.mail.CommentNotification;
import hu.psprog.leaflet.service.vo.ContactRequestVO;

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
     * Sends notification about comment creation.
     *
     * @param commentNotification domain object holding required parameters
     */
    void commentNotification(CommentNotification commentNotification);

    /**
     * Sends notification about contact requests.
     *
     * @param contactRequestVO domain object holding required parameters
     */
    void contactRequestReceived(ContactRequestVO contactRequestVO);
}
