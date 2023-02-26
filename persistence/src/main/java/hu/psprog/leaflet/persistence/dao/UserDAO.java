package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.UserRepository}.
 *
 * @author Peter Smith
 */
public interface UserDAO extends BaseDAO<User, Long>, SelfStatusAwareDAO<Long> {

    User findByEmail(String email);

    void updatePassword(Long id, String password);

    void updateRole(Long id, Role role);
}
