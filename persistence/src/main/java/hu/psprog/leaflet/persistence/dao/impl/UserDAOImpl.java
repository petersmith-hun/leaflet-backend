package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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
    public void updateLastLogin(String email) {
        ((UserRepository) jpaRepository).updateLastLogin(email);
    }

    @Transactional
    @Override
    public User updateOne(Long id, User updatedEntity) {

        return findUser(id)
                .map(currentUser -> {
                    currentUser.setDefaultLocale(updatedEntity.getDefaultLocale());
                    currentUser.setEmail(updatedEntity.getEmail());
                    currentUser.setLastModified(new Date());
                    currentUser.setUsername(updatedEntity.getUsername());
                    jpaRepository.flush();

                    return currentUser;
                })
                .orElse(null);
    }

    private Optional<User> findUser(Long id) {
        return Optional.ofNullable(jpaRepository.findOne(id));
    }
}
