package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.AuthorityToRoleConverter;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.helper.UserVOTestDataGenerator;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final UserVO USER_VO_WITH_ADMIN_ROLE = UserVO.getBuilder()
            .withAuthorities(Collections.singletonList(Authority.ADMIN))
            .build();

    private static final UserVO USER_VO_WITH_NO_LOGIN_ROLE = UserVO.getBuilder()
            .withAuthorities(Collections.singletonList(Authority.NO_LOGIN))
            .build();

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

    @Test
    public void testGetOne() throws ServiceException {

        // given
        Long userID = 1L;
        given(userDAO.findOne(userID)).willReturn(user);
        given(userToUserVOConverter.convert(user)).willReturn(userVO);

        // when
        UserVO result = userService.getOne(userID);

        // then
        assertThat(result, equalTo(userVO));
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

    @Test
    public void testGetAll() {

        // given
        given(userDAO.findAll()).willReturn(Collections.singletonList(user));
        given(userToUserVOConverter.convert(user)).willReturn(userVO);

        // when
        List<UserVO> result = userService.getAll();

        // then
        assertThat(result, equalTo(Collections.singletonList(userVO)));
    }

    @Test
    public void testCount() {

        // given
        Long count = 5L;
        given(userDAO.count()).willReturn(count);

        // when
        Long result = userService.count();

        // then
        assertThat(result, equalTo(count));
    }

    @Test
    public void testDeleteByIDWithSuccess() throws ServiceException {

        // given
        given(userDAO.exists(userVO.getId())).willReturn(true);

        // when
        userService.deleteByID(userVO.getId());

        // then
        verify(userDAO).delete(userVO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIDWithFailure() throws ServiceException {

        // given
        given(userDAO.exists(userVO.getId())).willReturn(false);

        // when
        userService.deleteByID(userVO.getId());

        // then
        // expected exception
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

    @Test(expected = ConstraintViolationException.class)
    public void testCreateShouldThrowConstraintViolationException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(DataIntegrityViolationException.class).when(userDAO).save(user);

        // when
        userService.createOne(userVO);

        // then
        // exception expected
    }

    @Test(expected = ServiceException.class)
    public void testCreateShouldThrowServiceException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(IllegalArgumentException.class).when(userDAO).save(user);

        // when
        userService.createOne(userVO);

        // then
        // exception expected
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

    @Test(expected = ConstraintViolationException.class)
    public void testUpdateShouldThrowConstraintViolationException() throws ServiceException {

        // given
        Long id = 1L;
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(DataIntegrityViolationException.class).when(userDAO).updateOne(id, user);

        // when
        userService.updateOne(id, userVO);

        // then
        // exception expected
    }

    @Test(expected = ServiceException.class)
    public void testUpdateShouldThrowServiceException() throws ServiceException {

        // given
        Long id = 1L;
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(IllegalArgumentException.class).when(userDAO).updateOne(id, user);

        // when
        userService.updateOne(id, userVO);

        // then
        // exception expected
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

    @Test
    public void shouldReclaimPasswordWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        String updatedPassword = "new password";
        given(userDAO.exists(id)).willReturn(true);

        // when
        userService.reclaimPassword(id, updatedPassword);

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

    @Test
    public void testSilentGetUserByEmailWithExistingUser() {

        // given
        String email = "lflt123test@leaflet.dev";
        User user = new User();
        UserVO userVO = new UserVO();
        given(userDAO.findByEmail(email)).willReturn(user);
        given(userToUserVOConverter.convert(user)).willReturn(userVO);

        // when
        UserVO result = userService.silentGetUserByEmail(email);

        // then
        assertThat(result, notNullValue());
        verify(userDAO).findByEmail(email);
        verify(userToUserVOConverter).convert(user);
    }

    @Test
    public void testSilentGetUserByEmailWithNonExistingUser() {

        // given
        String email = "lflt123test@leaflet.dev";
        given(userDAO.findByEmail(email)).willReturn(null);

        // when
        UserVO result = userService.silentGetUserByEmail(email);

        // then
        assertThat(result, nullValue());
        verify(userDAO).findByEmail(email);
        verify(userToUserVOConverter, never()).convert(any(User.class));
    }

    @Test
    public void shouldRegister() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.save(user)).willReturn(user);

        // when
        Long result = userService.register(userVO);

        // then
        assertThat(result, equalTo(user.getId()));
    }

    @Test(expected = ServiceException.class)
    public void shouldRegisterWithFailure() throws ServiceException {

        // when
        userService.register(USER_VO_WITH_ADMIN_ROLE);

        // then
        // exception expected
    }

    @Test
    public void shouldRegisterNoLogin() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(USER_VO_WITH_NO_LOGIN_ROLE)).willReturn(user);
        given(userDAO.save(user)).willReturn(user);

        // when
        Long result = userService.registerNoLogin(USER_VO_WITH_NO_LOGIN_ROLE);

        // then
        assertThat(result, equalTo(user.getId()));
    }

    @Test(expected = ServiceException.class)
    public void shouldRegisterNoLoginWithFailure() throws ServiceException {

        // when
        userService.registerNoLogin(USER_VO_WITH_ADMIN_ROLE);

        // then
        // exception expected
    }

    @Test
    public void shouldGetEntityPage() {

        // given
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        given(userDAO.findAll(any(Pageable.class))).willReturn(userPage);

        // when
        EntityPageVO<UserVO> result = userService.getEntityPage(1, 10, OrderDirection.ASC, UserVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }
}
