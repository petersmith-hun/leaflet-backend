package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.facade.UserRepositoryFacade;
import hu.psprog.leaflet.service.common.RunLevel;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
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
    private UserRepositoryFacade userRepository;

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

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithException() throws ServiceException {

        // given
        Long userID = 1L;
        given(userRepository.findOne(userID)).willReturn(null);

        // when
        userService.getOne(userID);

        // then
        // expected exception
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntity() throws ServiceException {

        // given
        given(userRepository.exists(userVO.getId())).willReturn(false);

        // when
        userService.deleteByEntity(userVO);

        // then
        // expected exception
        verify(userRepository, never()).delete(any());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByID() throws ServiceException {

        // given
        willThrow(IllegalArgumentException.class).given(userRepository).delete(userVO.getId());

        // when
        userService.deleteByID(userVO.getId());

        // then
        // expected exception
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        // when
        Long result = userService.createOne(userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userRepository).save(user);
        assertThat(result, equalTo(user.getId()));
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.save(user)).willReturn(null);

        // when
        userService.createOne(userVO);

        // then
        // expected exception
        verify(userVOToUserConverter).convert(userVO);
        verify(userRepository).save(user);
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.updateOne(user.getId(), user)).willReturn(user);

        // when
        userService.updateOne(user.getId(), userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter).convert(user);
        verify(userRepository).updateOne(user.getId(), user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithException() throws ServiceException {

        // given
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.updateOne(user.getId(), user)).willReturn(null);

        // when
        userService.updateOne(user.getId(), userVO);

        // then
        verify(userVOToUserConverter).convert(userVO);
        verify(userToUserVOConverter, never()).convert(user);
        verify(userRepository).updateOne(user.getId(), user);
    }

    @Test
    public void testInitializeWithSuccess() throws UserInitializationException, EntityCreationException, NoSuchFieldException, IllegalAccessException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userRepository.count()).willReturn(0L);
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);

        // when
        Long result = userService.initialize(userVO);

        // then
        assertThat(result, equalTo(user.getId()));
        verify(userVOToUserConverter).convert(userVO);
        verify(userRepository).save(user);
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
        verify(userRepository, never()).count();
        verify(userVOToUserConverter, never()).convert(userVO);
        verify(userRepository, never()).save(user);
    }

    @Test(expected = UserInitializationException.class)
    public void testInitializeWhenAppIsAlreadyInitialized() throws NoSuchFieldException, IllegalAccessException, UserInitializationException, EntityCreationException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userRepository.count()).willReturn(1L);

        // when
        userService.initialize(userVO);

        // then
        verify(userRepository).count();
        verify(userVOToUserConverter, never()).convert(userVO);
        verify(userRepository, never()).save(user);
    }

    @Test(expected = EntityCreationException.class)
    public void testInitializeWithCreationFailure() throws NoSuchFieldException, IllegalAccessException, UserInitializationException, EntityCreationException {

        // given
        Field runLevel = userService.getClass().getDeclaredField("runLevel");
        runLevel.setAccessible(true);
        runLevel.set(userService, RunLevel.INIT);
        given(userRepository.count()).willReturn(0L);
        given(userVOToUserConverter.convert(userVO)).willReturn(user);
        given(userRepository.save(user)).willReturn(null);

        // when
        Long result = userService.initialize(userVO);

        // then
        assertThat(result, equalTo(user.getId()));
        verify(userVOToUserConverter).convert(userVO);
        verify(userRepository).save(user);
    }

    @Test
    public void testEnableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userRepository.exists(id)).willReturn(true);

        // when
        userService.enable(id);

        // then
        verify(userRepository).exists(id);
        verify(userRepository).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEnableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userRepository.exists(id)).willReturn(false);

        // when
        userService.enable(id);

        // then
        verify(userRepository).exists(id);
        verify(userRepository, never()).enable(id);
    }

    @Test
    public void testDisableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userRepository.exists(id)).willReturn(true);

        // when
        userService.disable(id);

        // then
        verify(userRepository).exists(id);
        verify(userRepository).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDisableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(userRepository.exists(id)).willReturn(false);

        // when
        userService.disable(id);

        // then
        verify(userRepository).exists(id);
        verify(userRepository, never()).disable(id);
    }
}
