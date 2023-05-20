package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.User;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Optional;

import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.SECURITY_TEST_PROFILE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * Context configuration for security tests.
 *
 * @author Peter Smith
 */
@Profile(SECURITY_TEST_PROFILE)
@Configuration
@ComponentScan("hu.psprog.leaflet.service.security.evaluator")
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityTestContextConfiguration {

    static final String SECURITY_TEST_PROFILE = "securityTest";
    static final long USER_UID = 1L;
    static final long EDITOR_UID = 2L;
    static final long ADMIN_UID = 3L;
    static final long OTHER_USER_UID = 4L;
    static final long OTHER_EDITOR_UID = 5L;
    private static final Entry ENTRY = prepareEntry();
    private static final Comment COMMENT = prepareComment();

    @Bean
    public EntryDAO entryDAO() {

        EntryDAO mockedEntryDAO = Mockito.mock(EntryDAO.class);
        given(mockedEntryDAO.findById(anyLong())).willReturn(Optional.of(ENTRY));

        return mockedEntryDAO;
    }

    @Bean
    public CommentDAO commentDAO() {

        CommentDAO mockedCommentDAO = Mockito.mock(CommentDAO.class);
        given(mockedCommentDAO.findById(anyLong())).willReturn(Optional.of(COMMENT));

        return mockedCommentDAO;
    }

    @Bean
    public SecurityExpressionStub securityExpressionStub() {
        return new SecurityExpressionStub();
    }

    private static Entry prepareEntry() {

        return Entry.getBuilder()
                .withUser(User.getBuilder()
                        .withId(EDITOR_UID)
                        .build())
                .build();
    }

    private static Comment prepareComment() {

        return Comment.getBuilder()
                .withUser(User.getBuilder()
                        .withId(USER_UID)
                        .build())
                .build();
    }
}
