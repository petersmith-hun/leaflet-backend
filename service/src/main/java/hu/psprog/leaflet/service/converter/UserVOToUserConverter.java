package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts {@link UserVO} to {@link User} object.
 *
 * @author Peter Smith
 */
@Component
public class UserVOToUserConverter implements Converter<UserVO, User> {

    @Override
    public User convert(UserVO source) {

        return User.getBuilder()
                .withId(source.getId())
                .withCreated(source.getCreated())
                .withPassword(source.getPassword())
                .withUsername(source.getUsername())
                .withDefaultLocale(source.getLocale())
                .withEmail(source.getEmail())
                .withLastLogin(source.getLastLogin())
                .withRole(mapRole(source))
                .withEnabled(source.isEnabled())
                .build();
    }

    private Role mapRole(UserVO source) {

        return Objects.nonNull(source.getAuthorities())
                ? mapAuthorities(source)
                : null;

    }

    private Role mapAuthorities(UserVO source) {

        return source.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .orElse(null);
    }
}
