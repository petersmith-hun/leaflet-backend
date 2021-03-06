package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import hu.psprog.leaflet.service.validation.user.UserValidatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {UserDetailsService} for Spring Security.
 *
 * @author Peter Smith
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USERNAME_NOT_FOUND_MESSAGE_PATTERN = "User identified by username [%s] not found";

    private UserDAO userDAO;
    private UserValidatorChain userValidatorChain;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO, UserValidatorChain userValidatorChain) {
        this.userDAO = userDAO;
        this.userValidatorChain = userValidatorChain;
    }

    /**
     * Loads a user by its email address (instead of username).
     *
     * @param email email address to load user by
     * @return UserDetails object holding necessary user data
     * @throws UsernameNotFoundException when no user found by specified email address
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userDAO.findByEmail(email);

        if (!userValidatorChain.runChain(user)) {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE_PATTERN, email));
        }

        return ExtendedUserDetails.getBuilder()
                .withUsername(user.getEmail())
                .withPassword(user.getPassword())
                .withEnabled(user.isEnabled())
                .withAuthorities(AuthorityUtils.createAuthorityList(user.getRole().name()))
                .withID(user.getId())
                .withName(user.getUsername())
                .build();
    }
}
