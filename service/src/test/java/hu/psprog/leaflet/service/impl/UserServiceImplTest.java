package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.UserRepository;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.helper.UserVOTestDataGenerator;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserVOConverter userToUserVOConverter;

    @Mock
    private UserVOToUserConverter userVOToUserConverter;

    @InjectMocks
    private UserServiceImpl userService;

    private UserVOTestDataGenerator userVOTestDataGenerator = new UserVOTestDataGenerator();
    private UserEntityTestDataGenerator userEntityTestDataGenerator = new UserEntityTestDataGenerator();

    private UserVO userVO;
    private User user;

    @Before
    public void setup() {
        userVO = userVOTestDataGenerator.generate();
        user = userEntityTestDataGenerator.generate();
    }

    @Test
    public void testLoadByUsernameWithSuccess() {

        // given
        String email = user.getEmail();
        given(userRepository.findByEmail(email)).willReturn(user);

        // when
        userService.loadUserByUsername(email);

        // then
        verify(userRepository).findByEmail(email);
        verify(userToUserVOConverter).convert(user);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameWithException() {

        // given
        String email = user.getEmail();
        given(userRepository.findByEmail(email)).willReturn(null);

        // when
        userService.loadUserByUsername(email);

        // then
        // expected exception
        verify(userToUserVOConverter, never()).convert(user);
    }
}
