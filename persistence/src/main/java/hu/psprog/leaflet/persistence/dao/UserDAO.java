package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.UserRepository}.
 *
 * @author Peter Smith
 */
public interface UserDAO extends BaseDAO<User, Long>, SelfStatusAwareDAO<Long> {

    public User findByEmail(String email);

    public void updatePassword(Long id, String password);

    public void updateRole(Long id, Role role);
}
