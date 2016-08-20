package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * User repository interface for Hibernate JPA persistence manager.
 *
 * @author Peter Smith
 */
@Repository
public interface UserRepository extends SelfStatusAwareJpaRepository<User, Long> {

    /**
     * Returns {@link User} object identified by given email address.
     *
     * @param email user's email address
     * @return User object identified by given link or {@code null} if no User found
     */
    public User findByEmail(String email);

    /**
     * Updates given user's current password.
     *
     * @param id identifier of {@link User}
     * @param password new password
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    public void updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * Updates given user's current role.
     *
     * @param id identifier of {@link User}
     * @param role new role
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    public void updateRole(@Param("id") Long id, @Param("role") Role role);
}
