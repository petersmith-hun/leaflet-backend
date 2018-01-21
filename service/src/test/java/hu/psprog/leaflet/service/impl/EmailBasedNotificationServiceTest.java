package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.service.mail.MailFactory;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.impl.CommentNotificationMailFactory;
import hu.psprog.leaflet.service.mail.impl.MailFactoryRegistry;
import hu.psprog.leaflet.service.mail.impl.PasswordResetRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.PasswordResetSuccessMailFactory;
import hu.psprog.leaflet.service.mail.impl.SystemStartupMailFactory;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EmailBasedNotificationService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailBasedNotificationServiceTest {

    private static final Observable<MailDeliveryInfo> DELIVERY_INFO_OBSERVABLE = Observable.empty();

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

        // when
        emailBasedNotificationService.passwordResetRequested(PasswordResetRequest.getBuilder().build());

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendPasswordResetConfirmationNotification() {

        // given
        prepareMockFactory(passwordResetSuccessMailFactory);

        // when
        emailBasedNotificationService.successfulPasswordReset(PasswordResetSuccess.getBuilder().build());

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendCommentNotification() {

        // given
        prepareMockFactory(commentNotificationMailFactory);

        // when
        emailBasedNotificationService.commentNotification(CommentNotification.getBuilder().build());

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