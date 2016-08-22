package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Converts {@link GrantedAuthority} to {@link Role} object.
 *
 * @author Peter Smith
 */
@Component
public class AuthorityToRoleConverter implements Converter<GrantedAuthority, Role> {

    @Override
    public Role convert(GrantedAuthority source) {

        return Role.valueOf(source.getAuthority());
    }
}
