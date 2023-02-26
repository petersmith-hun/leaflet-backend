package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * DAO implementation for {@link UserRepository}.
 *
 * @author Peter Smith
 */
@Component
public class UserDAOImpl extends SelfStatusAwareDAOImpl<User, Long> implements UserDAO {

    @Autowired
    public UserDAOImpl(final UserRepository userRepository, JpaContext jpaContext) {
        super(userRepository, jpaContext);
    }

    @Override
    public User findByEmail(String email) {
        return ((UserRepository) jpaRepository).findByEmail(email);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    @Override
    public void updatePassword(Long id, String password) {
        findUser(id).ifPresent(user -> {
            ((UserRepository) jpaRepository).updatePassword(id, password);
            ((UserRepository) jpaRepository).updateLastModified(id);
        });
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    @Override
    public void updateRole(Long id, Role role) {
        findUser(id).ifPresent(user -> {
            ((UserRepository) jpaRepository).updateRole(id, role);
            ((UserRepository) jpaRepository).updateLastModified(id);
        });
    }

    @Override
    protected void doUpdate(User currentEntity, User updatedEntity) {
        
        currentEntity.setDefaultLocale(updatedEntity.getDefaultLocale());
        currentEntity.setEmail(updatedEntity.getEmail());
        currentEntity.setUsername(updatedEntity.getUsername());
    }

    private Optional<User> findUser(Long id) {
        return jpaRepository.findById(id);
    }
}
