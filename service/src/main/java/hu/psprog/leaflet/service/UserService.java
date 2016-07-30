package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.ModifiableUserVO;
import hu.psprog.leaflet.service.vo.SafeUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * User service operations interface.
 *
 * @author Peter Smith
 */
public interface UserService extends UserDetailsService,
        CreateOperationCapableService<ModifiableUserVO, Long>,
        ReadOperationCapableService<SafeUserVO, Long>,
        UpdateOperationCapableService<ModifiableUserVO, SafeUserVO, Long>,
        DeleteOperationCapableService<SafeUserVO, Long> {
}
