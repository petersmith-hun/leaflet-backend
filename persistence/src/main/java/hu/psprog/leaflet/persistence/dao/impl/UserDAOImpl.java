package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * DAO implementation for {@link UserRepository}.
 *
 * @author Peter Smith
 */
@Component
public class UserDAOImpl extends SelfStatusAwareDAOImpl<User, Long> implements UserDAO {

    @Autowired
    public UserDAOImpl(final UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public User findByEmail(String email) {
        return ((UserRepository )jpaRepository).findByEmail(email);
    }

    @Override
    public void updatePassword(Long id, String password) {
        ((UserRepository )jpaRepository).updatePassword(id, password);
    }

    @Override
    public void updateRole(Long id, Role role) {
        ((UserRepository )jpaRepository).updateRole(id, role);
    }

    @Override
    public User updateOne(Long id, User updatedEntity) {

        User updatedUser = null;
        User currentUser = jpaRepository.findOne(id);

        if (currentUser != null) {
            currentUser.setDefaultLocale(updatedEntity.getDefaultLocale());
            currentUser.setEmail(updatedEntity.getEmail());
            currentUser.setLastModified(new Date());
            currentUser.setUsername(updatedEntity.getUsername());
            jpaRepository.flush();

            updatedUser = jpaRepository.getOne(id);
        }

        return updatedUser;
    }
}
