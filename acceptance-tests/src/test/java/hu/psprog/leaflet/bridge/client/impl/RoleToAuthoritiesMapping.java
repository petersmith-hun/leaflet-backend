package hu.psprog.leaflet.bridge.client.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Email-to-authority (scope) list mapping.
 *
 * @author Peter Smith
 */
@Component
public class RoleToAuthoritiesMapping {

    private static final String[] USER_AUTHORITIES = {
            "read:comments:own",
            "write:comments:own"
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
    )).toList().toArray(String[]::new);

    private static final String[] ADMIN_AUTHORITIES = Stream.concat(Stream.of(EDITOR_AUTHORITIES), Stream.of(
            "read:admin",
            "write:admin"
    )).toList().toArray(String[]::new);

    private static final Map<String, String> ROLE_TO_AUTHORITY_LIST_MAP = Map.of(
            "test-user-1@ac-leaflet.local", String.join(StringUtils.SPACE, USER_AUTHORITIES),
            "test-user-3@ac-leaflet.local", String.join(StringUtils.SPACE, USER_AUTHORITIES),
            "test-user-6@ac-leaflet.local", String.join(StringUtils.SPACE, USER_AUTHORITIES),
            "test-user-7@ac-leaflet.local", String.join(StringUtils.SPACE, USER_AUTHORITIES),
            "test-editor-4@ac-leaflet.local", String.join(StringUtils.SPACE, EDITOR_AUTHORITIES),
            "test-editor-5@ac-leaflet.local", String.join(StringUtils.SPACE, EDITOR_AUTHORITIES),
            "test-editor-8@ac-leaflet.local", String.join(StringUtils.SPACE, EDITOR_AUTHORITIES),
            "test-admin@ac-leaflet.local", String.join(StringUtils.SPACE, ADMIN_AUTHORITIES)
    );

    /**
     * Returns the relevant scope for the given email.
     * Multiple scope values are chained together with space character.
     *
     * @param email email address to get scope for
     * @return scope chain as a single {@link String}
     */
    public String getAuthoritiesByEmail(String email) {
        return ROLE_TO_AUTHORITY_LIST_MAP.get(email);
    }
}
