package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserFacadeImplTest {

    private static final long USER_ID = 11L;
    private static final UserVO USER_VO = UserVO.wrapMinimumVO(USER_ID);
    private static final String PASSWORD = "test-pw";
    private static final String NEW_PASSWORD = "test-pw-new";
    private static final String ENCODED_PASSWORD = "test-pw-encoded";
    private static final UserVO USER_TO_CREATE = UserVO.getBuilder()
            .withPassword(PASSWORD)
            .build();
    private static final UserVO REBUILT_USER = UserVO.getBuilder()
            .withPassword(ENCODED_PASSWORD)
            .build();

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserFacadeImpl userFacade;

    @Test
    public void shouldGetUserList() {

        // when
        userFacade.getUserList();

        // then
        verify(userService).getAll();
    }

    @Test
    public void shouldCreateUser() throws ServiceException {

        // given
        given(passwordEncoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);
        given(userService.createOne(REBUILT_USER)).willReturn(USER_ID);

        // when
        userFacade.createUser(USER_TO_CREATE);

        // then
        verify(userService).createOne(REBUILT_USER);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldGetUserByID() throws ServiceException {

        // when
        userFacade.getUserByID(USER_ID);

        // then
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldDeleteUserByID() throws ServiceException {

        // when
        userFacade.deleteUserByID(USER_ID);

        // then
        verify(userService).deleteByID(USER_ID);
    }

    @ParameterizedTest
    @MethodSource("authorityDataProvider")
    public void shouldChangeAuthority(String authorityName, GrantedAuthority expectedAuthority) throws ServiceException {

        // when
        userFacade.changeAuthority(USER_ID, authorityName);

        // then
        verify(userService).changeAuthority(USER_ID, expectedAuthority);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldUpdateProfile() throws ServiceException {

        // when
        userFacade.updateUserProfile(USER_ID, USER_VO);

        // then
        verify(userService).updateOne(USER_ID, USER_VO);
        verify(userService).getOne(USER_ID);
    }

    @Test
    public void shouldUpdatePassword() throws ServiceException {

        // given
        given(passwordEncoder.encode(NEW_PASSWORD)).willReturn(ENCODED_PASSWORD);

        // when
        userFacade.updateUserPassword(USER_ID, PASSWORD, NEW_PASSWORD);

        // then
        verify(userService).changePassword(USER_ID, ENCODED_PASSWORD);
        verify(userService).getOne(USER_ID);
    }

    private static Stream<Arguments> authorityDataProvider() {
        
        return Stream.of(
                Arguments.of("ADMIN", Authority.ADMIN),
                Arguments.of("EDITOR", Authority.EDITOR),
                Arguments.of("SERVICE", Authority.SERVICE),
                Arguments.of("USER", Authority.USER)
        );
    }
}