package hu.psprog.leaflet.bridge.client.impl;

import hu.psprog.leaflet.persistence.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Role to authority (scope) list mapping.
 *
 * @author Peter Smith
 */
@Component
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

    private static final Map<Role, String> ROLE_TO_AUTHORITY_LIST_MAP = Map.of(
            Role.USER, String.join(StringUtils.SPACE, USER_AUTHORITIES),
            Role.EDITOR, String.join(StringUtils.SPACE, EDITOR_AUTHORITIES),
            Role.ADMIN, String.join(StringUtils.SPACE, ADMIN_AUTHORITIES)
    );

    /**
     * Returns the relevant scope for the given role.
     * Multiple scope values are chained together with space character.
     *
     * @param role {@link Role} to get scope for
     * @return scope chain as a single {@link String}
     */
    public String getAuthoritiesForRole(Role role) {
        return ROLE_TO_AUTHORITY_LIST_MAP.get(role);
    }
}
