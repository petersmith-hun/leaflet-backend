package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.service.mail.MailFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class MailFactoryRegistryTest {

    @Mock
    private CommentNotificationMailFactory commentNotificationMailFactory;

    @Mock
    private MessageSource messageSource;

    private SystemStartupMailFactory systemStartupMailFactory = new SystemStartupMailFactory(messageSource);

    private MailFactoryRegistry mailFactoryRegistry;

    @Before
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForUnknownFactory() {

        // when
        mailFactoryRegistry.getFactory(PasswordResetRequestMailFactory.class);

        // then
        // exception expected
    }
}