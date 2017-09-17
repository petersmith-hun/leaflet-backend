package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.security.core.GrantedAuthority;

/**
 * User service operations interface.
 *
 * @author Peter Smith
 */
public interface UserService extends CreateOperationCapableService<UserVO, Long>,
        ReadOperationCapableService<UserVO, Long>,
        UpdateOperationCapableService<UserVO, UserVO, Long>,
        DeleteOperationCapableService<UserVO, Long>,
        PageableService<UserVO, UserVO.OrderBy>,
        StatusChangeCapableService<Long> {

    /**
     * Creates a user via registration.
     *
     * @param entity user data
     * @return created user's ID
     * @throws ServiceException if user could not be created
     */
    Long register(UserVO entity) throws ServiceException;

    /**
     * Initializes user database with first administrator user. Application must be running in INIT mode,
     * and no user shall be existing!
     *
     * @param userVO {@link UserVO} object holding primary administrator user data
     * @return ID of created user
     * @throws UserInitializationException when application is not running in INIT mode, or there's already an existing primary administrator user
     */
    Long initialize(UserVO userVO) throws UserInitializationException, EntityCreationException;

    /**
     * Changes given user's password.
     *
     * @param id ID of user to update password for
     * @param password new password
     */
    void changePassword(Long id, String password) throws EntityNotFoundException;

    /**
     * Changes given user's authority (role).
     *
     * @param id ID of user to update role for
     * @param grantedAuthority new granted authority
     */
    void changeAuthority(Long id, GrantedAuthority grantedAuthority) throws EntityNotFoundException;

    /**
     * Updates given user's last login field.
     *
     * @param email email of user to update last login for
     * @throws EntityNotFoundException if no user found associated with given username
     */
    void updateLastLogin(String email) throws EntityNotFoundException;

    /**
     * Retrieves user identified by given email address.
     * Does NOT throw exception if entity not found, instead returns null!
     *
     * @param email email of user to find
     * @return identified user as {@link UserVO} object or null if not exists
     */
    UserVO silentGetUserByEmail(String email);
}
