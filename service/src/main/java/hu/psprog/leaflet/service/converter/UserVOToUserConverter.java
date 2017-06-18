package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Converts {@link UserVO} to {@link User} object.
 *
 * @author Peter Smith
 */
@Component
public class UserVOToUserConverter implements Converter<UserVO, User> {

    @Autowired
    private AuthorityToRoleConverter authorityToRoleConverter;

    @Override
    public User convert(UserVO source) {

        Role role = null;
        if (source.getAuthorities() != null) {
            Iterator<? extends GrantedAuthority> authorityIterator = source.getAuthorities().iterator();
            while (authorityIterator.hasNext()) {
                role = Role.valueOf(authorityIterator.next().getAuthority());
            }
        }

        return User.getBuilder()
                .withId(source.getId())
                .withCreated(source.getCreated())
                .withPassword(source.getPassword())
                .withUsername(source.getUsername())
                .withDefaultLocale(source.getLocale())
                .withEmail(source.getEmail())
                .withLastLogin(source.getLastLogin())
                .withRole(role)
                .withEnabled(source.isEnabled())
                .build();
    }
}
