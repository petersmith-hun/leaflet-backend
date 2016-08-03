package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.UserRepository;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService} interface.
 *
 * @author Peter Smith
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAME_NOT_FOUND_MESSAGE_PATTERN = "User identified by username [%s] not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    /**
     * Loads a user by its email address (instead of username).
     *
     * @param email email address to load user by
     * @return UserDetails object holding necessary user data
     * @throws UsernameNotFoundException when no user found by specified email address
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE_PATTERN, email));
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    public UserVO getOne(Long userID) throws EntityNotFoundException {

        User user = userRepository.findOne(userID);

        if(user == null) {
            throw new EntityNotFoundException(User.class, userID);
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    public List<UserVO> getAll() {

        return userRepository.findAll().stream()
                .map(user -> userToUserVOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByEntity(UserVO entity) {

    }

    @Override
    public void deleteByID(Long aLong) {

    }

    @Override
    public void deleteBulkByIDs(List<Long> longs) {

    }

    @Override
    public Long createOne(UserVO entity) {
        return null;
    }

    @Override
    public List<Long> createBulk(List<UserVO> entities) {
        return null;
    }

    @Override
    public UserVO updateOne(Long aLong, UserVO updatedEntity) {
        return null;
    }

    @Override
    public List<UserVO> updateBulk(Map<Long, UserVO> updatedEntities) {
        return null;
    }
}
