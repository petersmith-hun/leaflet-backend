package hu.psprog.leaflet.persistence.facade;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;

/**
 * @author Peter Smith
 */
public interface UserRepositoryFacade extends BaseRepositoryFacade<User, Long>, SelfStatusAwareRepositoryFacade<Long> {

    public User findByEmail(String email);

    public void updatePassword(Long id, String password);

    public void updateRole(Long id, Role role);
}
