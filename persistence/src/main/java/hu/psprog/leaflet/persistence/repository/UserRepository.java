package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns {@link User} object identified by given email address.
     *
     * @param email user's email address
     * @return User object identified by given link or {@code null} if no User found
     */
    public User findByEmail(String email);
}
