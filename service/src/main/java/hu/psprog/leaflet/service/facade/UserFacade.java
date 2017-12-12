package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import hu.psprog.leaflet.service.vo.UserVO;

import java.util.List;

/**
 * User operations facade.
 *
 * @author Peter Smith
 */
public interface UserFacade {

    /**
     * Returns {@link List} of {@link UserVO} object.
     *
     * @return list of users
     */
    List<UserVO> getUserList();

    /**
     * Creates a new user and returns its data.
     * Also deals with password hashing.
     *
     * @param userData user data
     * @return created user data
     * @throws ServiceException if user could not be created
     */
    UserVO createUser(UserVO userData) throws ServiceException;

    /**
     * Returns data of user identified by given ID.
     *
     * @param userID ID of the user to retrieve data of
     * @return user data
     * @throws ServiceException if requested user could not be found
     */
    UserVO getUserByID(Long userID) throws ServiceException;

    /**
     * Deletes user identified by given ID.
     *
     * @param userID ID of the user to delete
     * @throws ServiceException if user could not be found
     */
    void deleteUserByID(Long userID) throws ServiceException;

    /**
     * Changes given user's authority (role).
     *
     * @param userID ID of user to update role for
     * @param newRole user's new role
     * @return updated user data
     * @throws ServiceException if user could not be found
     */
    UserVO changeAuthority(Long userID, String newRole) throws ServiceException;

    /**
     * Updates given user's profile information then returns the updated user data.
     *
     * @param userID ID of the user to update profile of
     * @param updatedUserData updated user data
     * @return updated user data
     * @throws ServiceException if user could not be found
     */
    UserVO updateUserProfile(Long userID, UserVO updatedUserData) throws ServiceException;

    /**
     * Updates given user's password.
     * Before updating password, also validates current password.
     * Deals with password hashing as well.
     *
     * @param userID ID of the user to update password of
     * @param currentPassword current password for validation
     * @param newPassword new password
     * @return user data
     * @throws ServiceException if user could not be found
     */
    UserVO updateUserPassword(Long userID, String currentPassword, String newPassword) throws ServiceException;

    /**
     * Handles login process.
     *  - Claims user token.
     *  - Updates last login date of user.
     *
     * @param loginContext {@link LoginContextVO} object holding full login information (username, password, device ID and remote address)
     * @return token on successful login
     * @throws EntityNotFoundException if user could not be found
     */
    String login(LoginContextVO loginContext) throws EntityNotFoundException;

    /**
     * Registers a new user on public site.
     *
     * @param userData user data
     * @return ID of the created user
     * @throws ServiceException if failed to process registration (ex.: already registered email address)
     */
    Long register(UserVO userData) throws ServiceException;

    /**
     * Handles logout process.
     * Security Context will be used to retrieve user identification information.
     */
    void logout();

    /**
     * Starts password reset process.
     *
     * @param loginContext {@link LoginContextVO} object holding user information required for password reset (username, device ID and remote address)
     */
    void demandPasswordReset(LoginContextVO loginContext);

    /**
     * Confirms password reset request by updating password.
     * Also deals with password hashing.
     * Security Context will be used to retrieve user identification information.
     *
     * @param password new password
     * @throws EntityNotFoundException if user could not be found
     */
    void confirmPasswordReset(String password) throws EntityNotFoundException;

    /**
     * Requests session extension.
     * Security Context will be used to retrieve user identification information.
     *
     * @param loginContext {@link LoginContextVO} object holding information required for session extension (device ID and remote address)
     * @return generated new token
     */
    String extendSession(LoginContextVO loginContext);
}
