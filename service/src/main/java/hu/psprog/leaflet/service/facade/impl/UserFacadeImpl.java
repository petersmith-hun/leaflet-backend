package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link UserFacade}.
 *
 * @author Peter Smith
 */
@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserFacadeImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserVO> getUserList() {
        return userService.getAll();
    }

    @Override
    public UserVO createUser(UserVO userData) throws ServiceException {
        Long userID = userService.createOne(rebuildUserDataWithHashedPassword(userData));
        return userService.getOne(userID);
    }

    @Override
    public UserVO getUserByID(Long userID) throws ServiceException {
        return userService.getOne(userID);
    }

    @Override
    public void deleteUserByID(Long userID) throws ServiceException {
        userService.deleteByID(userID);
    }

    @Override
    public UserVO changeAuthority(Long userID, String newRole) throws ServiceException {
        userService.changeAuthority(userID, Authority.getAuthorityByName(newRole));
        return userService.getOne(userID);
    }

    @Override
    public UserVO updateUserProfile(Long userID, UserVO updatedUserData) throws ServiceException {
        userService.updateOne(userID, updatedUserData);
        return userService.getOne(userID);
    }

    @Override
    public UserVO updateUserPassword(Long userID, String currentPassword, String newPassword) throws ServiceException {

        userService.changePassword(userID, passwordEncoder.encode(newPassword));

        return userService.getOne(userID);
    }

    private UserVO rebuildUserDataWithHashedPassword(UserVO originalUserVO) {
        return UserVO.getBuilder()
                .withUsername(originalUserVO.getUsername())
                .withEmail(originalUserVO.getEmail())
                .withPassword(passwordEncoder.encode(originalUserVO.getPassword()))
                .withEnabled(originalUserVO.isEnabled())
                .withCreated(originalUserVO.getCreated())
                .withLocale(originalUserVO.getLocale())
                .withAuthorities(originalUserVO.getAuthorities())
                .build();
    }
}
