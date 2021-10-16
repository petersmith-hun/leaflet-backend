package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.validation.user.UserValidatorChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserDetailsServiceImpl} class.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock(lenient = true)
    private UserValidatorChain userValidatorChain;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private final UserEntityTestDataGenerator userEntityTestDataGenerator = new UserEntityTestDataGenerator();

    private User user;

    @BeforeEach
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

    @Test
    public void testLoadByUsernameWithException() {

        // given
        String email = user.getEmail();
        given(userDAO.findByEmail(email)).willReturn(null);
        given(userValidatorChain.runChain(user)).willReturn(false);

        // when
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));

        // then
        // expected exception
    }
}
