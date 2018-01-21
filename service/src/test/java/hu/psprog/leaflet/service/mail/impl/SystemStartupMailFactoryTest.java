package hu.psprog.leaflet.service.mail.impl;

import hu.psprog.leaflet.mail.domain.Mail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link SystemStartupMailFactory}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemStartupMailFactoryTest {

    private static final String SUBJECT = "Leaflet started up";
    private static final String TEMPLATE = "system_startup.html";

    private static final String VERSION = "version";
    private static final String GENERATED_AT = "generatedAt";

    @InjectMocks
    private SystemStartupMailFactory systemStartupMailFactory;

    @Test
    public void shouldBuildMail() {

        // when
        Mail result = systemStartupMailFactory.buildMail(VERSION);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getRecipient(), nullValue());
        assertThat(result.getSubject(), equalTo(SUBJECT));
        assertThat(result.getTemplate(), equalTo(TEMPLATE));
        assertThat(result.getContentMap().get(VERSION), equalTo(VERSION));
        assertThat(result.getContentMap().get(GENERATED_AT), notNullValue());
    }
}