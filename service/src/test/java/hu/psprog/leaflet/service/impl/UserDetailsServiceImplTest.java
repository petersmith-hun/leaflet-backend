package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.validation.user.UserValidatorChain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserDetailsServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock(lenient = true)
    private UserValidatorChain userValidatorChain;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserEntityTestDataGenerator userEntityTestDataGenerator = new UserEntityTestDataGenerator();
    private User user;

    @Before
    public void setup() {
        user = userEntityTestDataGenerator.generate();
    }

    @Test
    public void testLoadByUsernameWithSuccess() {

        // given
        String email = user.getEmail();
        given(userDAO.findByEmail(email)).willReturn(user);
        given(userValidatorChain.runChain(user)).willReturn(true);

        // when
        userDetailsService.loadUserByUsername(email);

        // then
        verify(userDAO).findByEmail(email);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameWithException() {

        // given
        String email = user.getEmail();
        given(userDAO.findByEmail(email)).willReturn(null);
        given(userValidatorChain.runChain(user)).willReturn(false);

        // when
        userDetailsService.loadUserByUsername(email);

        // then
        // expected exception
    }
}
