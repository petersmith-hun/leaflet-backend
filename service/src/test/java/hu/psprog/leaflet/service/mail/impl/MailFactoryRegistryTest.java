package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.service.mail.MailFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link MailFactoryRegistry}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class MailFactoryRegistryTest {

    @Mock
    private CommentNotificationMailFactory commentNotificationMailFactory;

    @Mock
    private MessageSource messageSource;

    private final SystemStartupMailFactory systemStartupMailFactory = new SystemStartupMailFactory(messageSource);

    private MailFactoryRegistry mailFactoryRegistry;

    @BeforeEach
    public void setup() {
        mailFactoryRegistry = new MailFactoryRegistry(Arrays.asList(commentNotificationMailFactory, systemStartupMailFactory));
    }

    @Test
    public void shouldReturnKnownFactory() {

        // when
        MailFactory<?> result = mailFactoryRegistry.getFactory(SystemStartupMailFactory.class);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(systemStartupMailFactory));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForUnknownFactory() {

        // when
        Assertions.assertThrows(IllegalArgumentException.class, () -> mailFactoryRegistry.getFactory(PasswordResetRequestMailFactory.class));

        // then
        // exception expected
    }
}