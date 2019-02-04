package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.impl.CommentNotificationMailFactory;
import hu.psprog.leaflet.service.mail.impl.ContactRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.MailFactoryRegistry;
import hu.psprog.leaflet.service.mail.impl.PasswordResetRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.PasswordResetSuccessMailFactory;
import hu.psprog.leaflet.service.mail.impl.SystemStartupMailFactory;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EmailBasedNotificationService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailBasedNotificationServiceTest {

    private static final Observable<MailDeliveryInfo> DELIVERY_INFO_OBSERVABLE = Observable.empty();
    private static final String DESTINATION_EMAIL_ADDRESS = "destination-email-address";

    @Mock
    private MailClient mailClient;

    @Mock
    private LoggingMailObserverHandler loggingMailObserverHandler;

    @Mock
    private MailFactoryRegistry mailFactoryRegistry;

    @Mock
    private SystemStartupMailFactory systemStartupMailFactory;

    @Mock
    private PasswordResetSuccessMailFactory passwordResetSuccessMailFactory;

    @Mock
    private PasswordResetRequestMailFactory passwordResetRequestMailFactory;

    @Mock
    private CommentNotificationMailFactory commentNotificationMailFactory;

    @Mock
    private ContactRequestMailFactory contactRequestMailFactory;

    @Mock
    private Mail mockMail;

    @InjectMocks
    private EmailBasedNotificationService emailBasedNotificationService;

    @Before
    public void setup() {
        given(mailClient.sendMail(mockMail)).willReturn(DELIVERY_INFO_OBSERVABLE);
    }

    @Test
    public void shouldSendStartupFinishedNotification() {

        // given
        prepareMockFactory(systemStartupMailFactory);

        // when
        emailBasedNotificationService.startupFinished("v1");

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendPasswordResetRequestedNotification() {

        // given
        prepareMockFactory(passwordResetRequestMailFactory);
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.getBuilder()
                .withParticipant(DESTINATION_EMAIL_ADDRESS)
                .build();

        // when
        emailBasedNotificationService.passwordResetRequested(passwordResetRequest);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendPasswordResetConfirmationNotification() {

        // given
        prepareMockFactory(passwordResetSuccessMailFactory);
        PasswordResetSuccess passwordResetSuccess = PasswordResetSuccess.getBuilder()
                .withParticipant(DESTINATION_EMAIL_ADDRESS)
                .build();

        // when
        emailBasedNotificationService.successfulPasswordReset(passwordResetSuccess);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendCommentNotification() {

        // given
        prepareMockFactory(commentNotificationMailFactory);
        CommentNotification commentNotification = CommentNotification.getBuilder()
                .withAuthorEmail(DESTINATION_EMAIL_ADDRESS)
                .build();

        // when
        emailBasedNotificationService.commentNotification(commentNotification);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendContactRequestNotification() {

        // given
        prepareMockFactory(contactRequestMailFactory);
        ContactRequestVO contactRequestVO = ContactRequestVO.getBuilder().build();

        // when
        emailBasedNotificationService.contactRequestReceived(contactRequestVO);

        // then
        assertMailSentAndObserverAttached();
    }

    private void assertMailSentAndObserverAttached() {
        verify(mailClient).sendMail(mockMail);
        verify(loggingMailObserverHandler).attachObserver(DELIVERY_INFO_OBSERVABLE);
    }

    private void prepareMockFactory(MailFactory<?> mockFactory) {
        given(mailFactoryRegistry.getFactory(any())).willReturn(mockFactory);
        given(mockFactory.buildMail(any())).willReturn(mockMail);
        given(mockFactory.buildMail(any(), anyString())).willReturn(mockMail);
    }
}