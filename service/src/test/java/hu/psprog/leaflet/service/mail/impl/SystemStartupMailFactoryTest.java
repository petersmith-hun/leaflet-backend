package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link SystemStartupMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemStartupMailFactoryTest {

    private static final String SUBJECT = "mail.system.event.startup.subject";
    private static final String TRANSLATED_SUBJECT = "Leaflet started up";
    private static final String TEMPLATE = "system_startup.html";

    private static final String VERSION = "version";
    private static final String GENERATED_AT = "generatedAt";
    private static final Locale FORCED_LOCALE = Locale.ENGLISH;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SystemStartupMailFactory systemStartupMailFactory;

    @Test
    public void shouldBuildMail() {

        // given
        systemStartupMailFactory.setForcedLocale(FORCED_LOCALE);
        given(messageSource.getMessage(SUBJECT, null, FORCED_LOCALE)).willReturn(TRANSLATED_SUBJECT);

        // when
        Mail result = systemStartupMailFactory.buildMail(VERSION);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), nullValue());
        assertThat(result.getSubject(), equalTo(TRANSLATED_SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getContentMap().get(VERSION), equalTo(VERSION));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }
}