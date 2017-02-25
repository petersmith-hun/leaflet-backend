package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.converter.AuthorityToRoleConverter;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.helper.UserVOTestDataGenerator;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserToUserVOConverter userToUserVOConverter;

    @Mock
    private UserVOToUserConverter userVOToUserConverter;

    @Mock
    private AuthorityToRoleConverter authorityToRoleConverter;

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

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithException() throws ServiceException {

        // given
        Long userID = 1L;
        given(userDAO.findOne(userID)).willReturn(null);

        // when
        userService.getOne(userID);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntity() throws ServiceException {

        // given
        given(userDAO.exists(userVO.getId())).willReturn(false);

        // when
        userService.deleteByEntity(userVO);

        // then
        // expected exception
        verify(userDAO, never()).delete(any());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByID() throws ServiceException {

        // given
        willThrow(IllegalArgumentException.class).given(userDAO).delete(userVO.getId());

        // when
        userService.deleteByID(userVO.getId());

        // then
        // expected exception
        verify(userDAO, never()).delete(any());
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.save(user)).willReturn(user);

        // when
        Long result = userService.createOne(userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userDAO).save(user);
        assertThat(result, equalTo(user.getId()));
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.save(user)).willReturn(null);

        // when
        userService.createOne(userVO);

        // then
        // expected exception
        verify(userVOToUserConverter).convert(userVO);
        verify(userDAO).save(user);
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.updateOne(user.getId(), user)).willReturn(user);

        // when
        userService.updateOne(user.getId(), userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter).convert(user);
        verify(userDAO).updateOne(user.getId(), user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.updateOne(user.getId(), user)).willReturn(null);

        // when
        userService.updateOne(user.getId(), userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter, never()).convert(user);
        verify(userDAO).updateOne(user.getId(), user);
    }

    @Test
    public void testInitializeWithSuccess() throws UserInitializationException, EntityCreationException, NoSuchFieldException, IllegalAccessException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userDAO.count()).willReturn(0L);
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.save(user)).willReturn(user);

        // when
        Long result = userService.initialize(userVO);

        // then
        assertThat(result, equalTo(user.getId()));
        verify(userVOToUserConverter).convert(userVO);
        verify(userDAO).save(user);
    }

    @Test(expected = UserInitializationException.class)
    public void testInitializeWhenAppIsNotInInitMode() throws UserInitializationException, EntityCreationException, NoSuchFieldException, IllegalAccessException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.PRODUCTION);

        // when
        userService.initialize(userVO);

        // then
        verify(userDAO, never()).count();
        verify(userVOToUserConverter, never()).convert(userVO);
        verify(userDAO, never()).save(user);
    }

    @Test(expected = UserInitializationException.class)
    public void testInitializeWhenAppIsAlreadyInitialized() throws NoSuchFieldException, IllegalAccessException, UserInitializationException, EntityCreationException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userDAO.count()).willReturn(1L);

        // when
        userService.initialize(userVO);

        // then
        verify(userDAO).count();
        verify(userVOToUserConverter, never()).convert(userVO);
        verify(userDAO, never()).save(user);
    }

    @Test(expected = EntityCreationException.class)
    public void testInitializeWithCreationFailure() throws NoSuchFieldException, IllegalAccessException, UserInitializationException, EntityCreationException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userDAO.count()).willReturn(0L);
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.save(user)).willReturn(null);

        // when
        Long result = userService.initialize(userVO);

        // then
        assertThat(result, equalTo(user.getId()));
        verify(userVOToUserConverter).convert(userVO);
        verify(userDAO).save(user);
    }

    @Test
    public void testChangePasswordWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        String updatedPassword = "new password";
        given(userDAO.exists(id)).willReturn(true);

        // when
        userService.changePassword(id, updatedPassword);

        // then
        verify(userDAO).updatePassword(id, updatedPassword);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testChangePasswordWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        String updatedPassword = "new password";
        given(userDAO.exists(id)).willReturn(false);

        // when
        userService.changePassword(id, updatedPassword);

        // then
        verify(userDAO, never()).updatePassword(id, updatedPassword);
    }

    @Test
    public void testChangeAuthorityWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(true);
        given(authorityToRoleConverter.convert(Authority.ADMIN)).willReturn(Role.ADMIN);

        // when
        userService.changeAuthority(id, Authority.ADMIN);

        // then
        verify(userDAO).updateRole(id, Role.ADMIN);
        verify(authorityToRoleConverter).convert(Authority.ADMIN);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testChangeAuthorityWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        userService.changeAuthority(id, Authority.ADMIN);

        // then
        verify(userDAO, never()).updateRole(id, Role.ADMIN);
        verify(authorityToRoleConverter, never()).convert(Authority.ADMIN);
    }

    @Test
    public void testEnableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(true);

        // when
        userService.enable(id);

        // then
        verify(userDAO).exists(id);
        verify(userDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEnableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        userService.enable(id);

        // then
        verify(userDAO).exists(id);
        verify(userDAO, never()).enable(id);
    }

    @Test
    public void testDisableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(true);

        // when
        userService.disable(id);

        // then
        verify(userDAO).exists(id);
        verify(userDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDisableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        userService.disable(id);

        // then
        verify(userDAO).exists(id);
        verify(userDAO, never()).disable(id);
    }

    @Test
    public void testUpdateLastLoginWithSuccess() throws EntityNotFoundException {

        // given
        String email = "lflt66test@leaflet.dev";
        given(userDAO.findByEmail(email)).willReturn(new User());

        // when
        userService.updateLastLogin(email);

        // then
        verify(userDAO).findByEmail(email);
        verify(userDAO).updateLastLogin(email);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateLastLoginWithNonExistingUser() throws EntityNotFoundException {

        // given
        String email = "lflt66test@leaflet.dev";
        given(userDAO.findByEmail(email)).willReturn(null);

        // when
        userService.updateLastLogin(email);

        // then
        // expected exception
        verify(userDAO).findByEmail(email);
        verify(userDAO, never()).updateLastLogin(anyString());
    }
}
