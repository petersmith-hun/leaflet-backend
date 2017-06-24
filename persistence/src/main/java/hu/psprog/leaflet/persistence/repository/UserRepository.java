package hu.psprog.leaflet.persistence.repository;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    User findByEmail(String email);

    /**
     * Updates given user's current password.
     *
     * @param id identifier of {@link User}
     * @param password new password
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * Updates given user's current role.
     *
     * @param id identifier of {@link User}
     * @param role new role
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRole(@Param("id") Long id, @Param("role") Role role);

    /**
     * Updates last login date of given user.
     *
     * @param email email address of {@link User}
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.lastLogin = now() WHERE u.email = :email")
    void updateLastLogin(@Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.lastModified = now() WHERE u.id = :id")
    void updateLastModified(@Param("id") Long id);
}
