package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.converter.RoleToAuthorityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Implementation of {UserDetailsService} for Spring Security.
 *
 * @author Peter Smith
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USERNAME_NOT_FOUND_MESSAGE_PATTERN = "User identified by username [%s] not found";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleToAuthorityConverter roleToAuthorityConverter;

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

        if(user == null) {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE_PATTERN, email));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                Arrays.asList(roleToAuthorityConverter.convert(user.getRole()))
        );
    }
}
