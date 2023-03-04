package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
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
     * Creates a no-login user via anonymous commenting.
     *
     * @param entity user data
     * @return created user's ID
     * @throws ServiceException if user could not be created
     */
    Long registerNoLogin(UserVO entity) throws ServiceException;

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
     * Retrieves user identified by given email address.
     * Does NOT throw exception if entity not found, instead returns null!
     *
     * @param email email of user to find
     * @return identified user as {@link UserVO} object or null if not exists
     */
    UserVO silentGetUserByEmail(String email);
}
