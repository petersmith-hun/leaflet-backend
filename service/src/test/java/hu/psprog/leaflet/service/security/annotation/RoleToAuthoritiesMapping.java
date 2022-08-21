package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.persistence.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Role to scope mapping for security tests.
 *
 * @author Peter Smith
 */
public class RoleToAuthoritiesMapping {

    private static final String[] USER_AUTHORITIES = {
            "read:users:own",
            "read:comments:own",
            "write:comments:own",
            "write:users:own"
    };

    private static final String[] EDITOR_AUTHORITIES = Stream.concat(Stream.of(USER_AUTHORITIES), Stream.of(
            "read:categories",
            "read:comments",
            "read:documents",
            "read:entries",
            "read:tags",
            "write:categories",
            "write:comments",
            "write:documents",
            "write:entries",
            "write:tags"
    )).collect(Collectors.toList()).toArray(String[]::new);

    private static final String[] ADMIN_AUTHORITIES = Stream.concat(Stream.of(EDITOR_AUTHORITIES), Stream.of(
            "read:admin",
            "read:users",
            "write:admin",
            "write:users"
    )).collect(Collectors.toList()).toArray(String[]::new);

    private static final Map<Role, List<GrantedAuthority>> ROLE_TO_AUTHORITY_LIST_MAP = Map.of(
            Role.USER, AuthorityUtils.createAuthorityList(applyScopePrefix(USER_AUTHORITIES)),
            Role.EDITOR, AuthorityUtils.createAuthorityList(applyScopePrefix(EDITOR_AUTHORITIES)),
            Role.ADMIN, AuthorityUtils.createAuthorityList(applyScopePrefix(ADMIN_AUTHORITIES))
    );

    public static List<GrantedAuthority> getAuthoritiesForRole(Role role) {
        return ROLE_TO_AUTHORITY_LIST_MAP.get(role);
    }

    private static String[] applyScopePrefix(String[] scopeArray) {

        return Stream.of(scopeArray)
                .map(scope -> String.format("SCOPE_%s", scope))
                .toArray(String[]::new);
    }
}
