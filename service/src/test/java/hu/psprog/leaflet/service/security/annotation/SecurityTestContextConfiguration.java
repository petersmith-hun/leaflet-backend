package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static hu.psprog.leaflet.service.security.annotation.SecurityTestContextConfiguration.SECURITY_TEST_PROFILE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

/**
 * Context configuration for security tests.
 *
 * @author Peter Smith
 */
@Profile(SECURITY_TEST_PROFILE)
@Configuration
@ComponentScan("hu.psprog.leaflet.service.security.evaluator")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityTestContextConfiguration extends WebSecurityConfigurerAdapter {

    static final String SECURITY_TEST_PROFILE = "securityTest";
    static final long CURRENT_USER_ID = 2L;

    @Bean
    public EntryService entryService() throws ServiceException {

        EntryService mockedEntryService = Mockito.mock(EntryService.class);
        given(mockedEntryService.getOne(anyLong())).willReturn(EntryVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(CURRENT_USER_ID)
                        .build())
                .build());

        return mockedEntryService;
    }

    @Bean
    public CommentService commentServiceImpl() throws ServiceException {

        CommentService mockedCommentService = Mockito.mock(CommentService.class);
        given(mockedCommentService.getOne(anyLong())).willReturn(CommentVO.getBuilder()
                .withOwner(UserVO.getBuilder()
                        .withId(CURRENT_USER_ID)
                        .build())
                .build());

        return mockedCommentService;
    }

    @Bean
    public SecurityExpressionStub securityExpressionStub() {
        return new SecurityExpressionStub();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationManagerBean();
    }
}
