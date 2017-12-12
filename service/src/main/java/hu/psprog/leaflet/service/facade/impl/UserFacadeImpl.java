package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.UserAuthenticationService;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.UserFacade;
import hu.psprog.leaflet.service.vo.LoginContextVO;
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

    private UserService userService;
    private UserAuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserFacadeImpl(UserService userService, UserAuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationService = authenticationService;
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
        // TODO checking currentPassword will be implemented by LFLT-182
        updateUserPassword(userID, newPassword);
        return userService.getOne(userID);
    }

    @Override
    public String login(LoginContextVO loginContext) throws EntityNotFoundException {

        String token = authenticationService.claimToken(loginContext);
        userService.updateLastLogin(loginContext.getUsername());

        return token;
    }

    @Override
    public Long register(UserVO userData) throws ServiceException {
        return userService.register(rebuildUserDataWithHashedPassword(userData));
    }

    @Override
    public void logout() {
        authenticationService.revokeToken();
    }

    @Override
    public void demandPasswordReset(LoginContextVO loginContext) {
        authenticationService.demandPasswordReset(loginContext);
    }

    @Override
    public void confirmPasswordReset(String password) throws EntityNotFoundException {

        Long userID = authenticationService.confirmPasswordReset();
        updateUserPassword(userID, password);
    }

    @Override
    public String extendSession(LoginContextVO loginContext) {
        return authenticationService.extendSession(loginContext);
    }

    private void updateUserPassword(Long userID, String newPassword) throws EntityNotFoundException {
        userService.changePassword(userID, passwordEncoder.encode(newPassword));
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
