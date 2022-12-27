package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.ContactService;
import hu.psprog.leaflet.service.NotificationService;
import hu.psprog.leaflet.service.vo.ContactRequestBlacklistRule;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link ContactService} implementation that processes contact requests by sending them in email (via {@link NotificationService}).
 *
 * @author Peter Smith
 */
@Service
@ConfigurationProperties(prefix = "mail.event.contact-request")
public class MailingContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailingContactServiceImpl.class);

    private final NotificationService notificationService;

    private List<ContactRequestBlacklistRule> blacklist = Collections.emptyList();

    @Autowired
    public MailingContactServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void processContactRequest(ContactRequestVO contactRequestVO) {

        if (isBlacklisted(contactRequestVO)) {
            LOGGER.warn("Contact request={} blocked by blacklist rule", contactRequestVO);
        } else {
            notificationService.contactRequestReceived(contactRequestVO);
        }
    }

    void setBlacklist(List<String> blacklistRules) {

        this.blacklist = blacklistRules.stream()
                .map(ContactRequestBlacklistRule::parseRule)
                .peek(rule -> LOGGER.info("Activated contact request blacklist rule: {}", rule))
                .collect(Collectors.toList());
    }

    private boolean isBlacklisted(ContactRequestVO contactRequestVO) {

        return blacklist.stream()
                .anyMatch(rule -> rule.match(contactRequestVO));
    }
}
