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
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.UserEntityTestDataGenerator;
import hu.psprog.leaflet.service.helper.UserVOTestDataGenerator;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserServiceImpl} class.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
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

    private final UserVOTestDataGenerator userVOTestDataGenerator = new UserVOTestDataGenerator();
    private final UserEntityTestDataGenerator userEntityTestDataGenerator = new UserEntityTestDataGenerator();

    private UserVO userVO;
    private User user;

    @BeforeEach
    public void setup() {
        userVO = userVOTestDataGenerator.generate();
        user = userEntityTestDataGenerator.generate();
    }

    @Test
    public void testGetOne() throws ServiceException {

        // given
        Long userID = 1L;
        given(userDAO.findById(userID)).willReturn(Optional.of(user));
        given(userToUserVOConverter.convert(user)).willReturn(userVO);

        // when
        UserVO result = userService.getOne(userID);

        // then
        assertThat(result, equalTo(userVO));
    }

    @Test
    public void testGetOneWithException() {

        // given
        Long userID = 1L;
        given(userDAO.findById(userID)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getOne(userID));

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
    public void testDeleteByIDWithSuccess() throws ServiceException {

        // given
        given(userDAO.exists(userVO.getId())).willReturn(true);

        // when
        userService.deleteByID(userVO.getId());

        // then
        verify(userDAO).delete(userVO.getId());
    }

    @Test
    public void testDeleteByIDWithFailure() {

        // given
        given(userDAO.exists(userVO.getId())).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.deleteByID(userVO.getId()));

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

    @Test
    public void testCreateShouldThrowConstraintViolationException() {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(DataIntegrityViolationException.class).when(userDAO).save(user);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> userService.createOne(userVO));

        // then
        // exception expected
    }

    @Test
    public void testCreateShouldThrowServiceException() {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(IllegalArgumentException.class).when(userDAO).save(user);

        // when
        Assertions.assertThrows(ServiceException.class, () -> userService.createOne(userVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.updateOne(user.getId(), user)).willReturn(Optional.of(user));
        given(userToUserVOConverter.convert(user)).willReturn(userVO);

        // when
        userService.updateOne(user.getId(), userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter).convert(user);
        verify(userDAO).updateOne(user.getId(), user);
    }

    @Test
    public void testUpdateOneWithException() {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userDAO.updateOne(user.getId(), user)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.updateOne(user.getId(), userVO));

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter, never()).convert(user);
        verify(userDAO).updateOne(user.getId(), user);
    }

    @Test
    public void testUpdateShouldThrowConstraintViolationException() {

        // given
        Long id = 1L;
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(DataIntegrityViolationException.class).when(userDAO).updateOne(id, user);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> userService.updateOne(id, userVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateShouldThrowServiceException() {

        // given
        Long id = 1L;
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        doThrow(IllegalArgumentException.class).when(userDAO).updateOne(id, user);

        // when
        Assertions.assertThrows(ServiceException.class, () -> userService.updateOne(id, userVO));

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
    public void testChangePasswordWithFailure() {

        // given
        Long id = 1L;
        String updatedPassword = "new password";
        given(userDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.changePassword(id, updatedPassword));

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

    @Test
    public void testChangeAuthorityWithFailure() {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.changeAuthority(id, Authority.ADMIN));

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

    @Test
    public void testEnableWithFailure() {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.enable(id));

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

    @Test
    public void testDisableWithFailure() {

        // given
        Long id = 1L;
        given(userDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.disable(id));

        // then
        verify(userDAO).exists(id);
        verify(userDAO, never()).disable(id);
    }

    @Test
    public void testSilentGetUserByEmailWithExistingUser() {

        // given
        String email = "lflt123test@leaflet.dev";
        User user = new User();
        UserVO userVO = UserVO.wrapMinimumVO(1L);
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
