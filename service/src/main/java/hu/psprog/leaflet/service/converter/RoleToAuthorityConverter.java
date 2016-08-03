package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Converts a user {@link Role} to {@link SimpleGrantedAuthority} object.
 *
 * @author Peter Smith
 */
@Component
public class RoleToAuthorityConverter implements Converter<Role, GrantedAuthority> {

    @Override
    public GrantedAuthority convert(Role source) {

        if(source == null) {
            return null;
        }

        return new SimpleGrantedAuthority(source.name());
    }
}
