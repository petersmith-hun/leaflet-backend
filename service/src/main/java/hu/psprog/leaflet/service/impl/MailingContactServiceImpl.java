package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.ContactService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link ContactService} implementation that processes contact requests by sending them in email (via {@link NotificationService}).
 *
 * @author Peter Smith
 */
@Service
public class MailingContactServiceImpl implements ContactService {

    private NotificationService notificationService;

    @Autowired
    public MailingContactServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void processContactRequest(ContactRequestVO contactRequestVO) {
        notificationService.contactRequestReceived(contactRequestVO);
    }
}
