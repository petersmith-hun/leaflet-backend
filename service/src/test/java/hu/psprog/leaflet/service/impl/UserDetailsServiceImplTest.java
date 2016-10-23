package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.converter.RoleToAuthorityConverter;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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

    @Mock
    private RoleToAuthorityConverter roleToAuthorityConverter;

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
        given(roleToAuthorityConverter.convert(user.getRole())).willReturn(Authority.USER);

        // when
        userDetailsService.loadUserByUsername(email);

        // then
        verify(userDAO).findByEmail(email);
        verify(roleToAuthorityConverter).convert(user.getRole());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameWithException() {

        // given
        String email = user.getEmail();
        given(userDAO.findByEmail(email)).willReturn(null);

        // when
        userDetailsService.loadUserByUsername(email);

        // then
        // expected exception
    }
}
