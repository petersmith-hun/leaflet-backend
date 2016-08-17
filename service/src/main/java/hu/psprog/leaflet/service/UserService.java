package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.PageableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * User service operations interface.
 *
 * @author Peter Smith
 */
public interface UserService extends UserDetailsService,
        CreateOperationCapableService<UserVO, Long>,
        ReadOperationCapableService<UserVO, Long>,
        UpdateOperationCapableService<UserVO, UserVO, Long>,
        DeleteOperationCapableService<UserVO, Long>,
        PageableService<UserVO, UserVO.OrderBy> {

    /**
     * Initializes user database with first administrator user. Application must be running in INIT mode,
     * and no user shall be existing!
     *
     * @param userVO {@link UserVO} object holding primary administrator user data
     * @return ID of created user
     * @throws UserInitializationException when application is not running in INIT mode, or there's already an existing primary administrator user
     */
    public Long initialize(UserVO userVO) throws UserInitializationException, EntityCreationException;
}
