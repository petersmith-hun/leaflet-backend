package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.mail.client.MailClient;
import hu.psprog.leaflet.mail.domain.Mail;
import hu.psprog.leaflet.mail.domain.MailDeliveryInfo;
import hu.psprog.leaflet.service.mail.domain.CommentNotification;
import hu.psprog.leaflet.service.mail.domain.PasswordResetRequest;
import hu.psprog.leaflet.service.mail.domain.PasswordResetSuccess;
import hu.psprog.leaflet.service.mail.domain.SignUpConfirmation;
import hu.psprog.leaflet.service.mail.impl.CommentNotificationMailFactory;
import hu.psprog.leaflet.service.mail.impl.ContactRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.MailFactoryRegistry;
import hu.psprog.leaflet.service.mail.impl.PasswordResetRequestMailFactory;
import hu.psprog.leaflet.service.mail.impl.PasswordResetSuccessMailFactory;
import hu.psprog.leaflet.service.mail.impl.SignUpConfirmationMailFactory;
import hu.psprog.leaflet.service.mail.impl.SystemStartupMailFactory;
import hu.psprog.leaflet.service.observer.impl.LoggingMailObserverHandler;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import io.reactivex.Observable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EmailBasedNotificationService}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EmailBasedNotificationServiceTest {

    private static final Observable<MailDeliveryInfo> DELIVERY_INFO_OBSERVABLE = Observable.empty();
    private static final String DESTINATION_EMAIL_ADDRESS = "destination-email-address";
    private static final String USER_1 = "user1";
    private static final String VERSION = "v1";

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
    private SignUpConfirmationMailFactory signUpConfirmationMailFactory;

    @Mock
    private Mail mockMail;

    @InjectMocks
    private EmailBasedNotificationService emailBasedNotificationService;

    @BeforeEach
    public void setup() {
        given(mailClient.sendMail(mockMail)).willReturn(DELIVERY_INFO_OBSERVABLE);
    }

    @Test
    public void shouldSendStartupFinishedNotification() {

        // given
        given(mailFactoryRegistry.getFactory(SystemStartupMailFactory.class)).willReturn(systemStartupMailFactory);
        given(systemStartupMailFactory.buildMail(VERSION)).willReturn(mockMail);

        // when
        emailBasedNotificationService.startupFinished(VERSION);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendPasswordResetRequestedNotification() {

        // given
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.getBuilder()
                .withParticipant(DESTINATION_EMAIL_ADDRESS)
                .build();
        given(mailFactoryRegistry.getFactory(PasswordResetRequestMailFactory.class)).willReturn(passwordResetRequestMailFactory);
        given(passwordResetRequestMailFactory.buildMail(passwordResetRequest, passwordResetRequest.getParticipant())).willReturn(mockMail);

        // when
        emailBasedNotificationService.passwordResetRequested(passwordResetRequest);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendPasswordResetConfirmationNotification() {

        // given
        PasswordResetSuccess passwordResetSuccess = PasswordResetSuccess.getBuilder()
                .withUsername(USER_1)
                .withParticipant(DESTINATION_EMAIL_ADDRESS)
                .build();
        given(mailFactoryRegistry.getFactory(PasswordResetSuccessMailFactory.class)).willReturn(passwordResetSuccessMailFactory);
        given(passwordResetSuccessMailFactory.buildMail(passwordResetSuccess.getUsername(), passwordResetSuccess.getParticipant())).willReturn(mockMail);

        // when
        emailBasedNotificationService.successfulPasswordReset(passwordResetSuccess);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendCommentNotification() {

        // given
        CommentNotification commentNotification = CommentNotification.getBuilder()
                .withAuthorEmail(DESTINATION_EMAIL_ADDRESS)
                .build();
        given(mailFactoryRegistry.getFactory(CommentNotificationMailFactory.class)).willReturn(commentNotificationMailFactory);
        given(commentNotificationMailFactory.buildMail(commentNotification, commentNotification.getAuthorEmail())).willReturn(mockMail);

        // when
        emailBasedNotificationService.commentNotification(commentNotification);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendContactRequestNotification() {

        // given
        ContactRequestVO contactRequestVO = ContactRequestVO.getBuilder().build();
        given(mailFactoryRegistry.getFactory(ContactRequestMailFactory.class)).willReturn(contactRequestMailFactory);
        given(contactRequestMailFactory.buildMail(contactRequestVO)).willReturn(mockMail);

        // when
        emailBasedNotificationService.contactRequestReceived(contactRequestVO);

        // then
        assertMailSentAndObserverAttached();
    }

    @Test
    public void shouldSendSignUpConfirmation() {

        // given
        SignUpConfirmation signUpConfirmation = new SignUpConfirmation("username", "email");
        given(mailFactoryRegistry.getFactory(SignUpConfirmationMailFactory.class)).willReturn(signUpConfirmationMailFactory);
        given(signUpConfirmationMailFactory.buildMail(signUpConfirmation, signUpConfirmation.getEmail())).willReturn(mockMail);

        // when
        emailBasedNotificationService.signUpConfirmation(signUpConfirmation);

        // then
        assertMailSentAndObserverAttached();
    }

    private void assertMailSentAndObserverAttached() {
        verify(mailClient).sendMail(mockMail);
        verify(loggingMailObserverHandler).attachObserver(DELIVERY_INFO_OBSERVABLE);
    }
}