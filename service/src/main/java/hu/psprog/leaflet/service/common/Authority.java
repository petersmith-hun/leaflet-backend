package hu.psprog.leaflet.service.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Known authority types.
 *
 * @author Peter Smith
 */
public class Authority {

    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority(AuthorityName.ADMIN.name());
    public static final GrantedAuthority USER = new SimpleGrantedAuthority(AuthorityName.USER.name());
    public static final GrantedAuthority EDITOR = new SimpleGrantedAuthority(AuthorityName.EDITOR.name());
    public static final GrantedAuthority SERVICE = new SimpleGrantedAuthority(AuthorityName.SERVICE.name());
    public static final GrantedAuthority NO_LOGIN = new SimpleGrantedAuthority(AuthorityName.NO_LOGIN.name());

    private Authority() {}

    public static GrantedAuthority getAuthorityByName(String authorityName) {

        switch (Authority.AuthorityName.valueOf(authorityName)) {
            case ADMIN:
                return Authority.ADMIN;
            case EDITOR:
                return Authority.EDITOR;
            case SERVICE:
                return Authority.SERVICE;
            case NO_LOGIN:
                return Authority.NO_LOGIN;
            case USER:
            default:
                return Authority.USER;
        }
    }

    private enum AuthorityName {
        ADMIN,
        USER,
        EDITOR,
        SERVICE,
        NO_LOGIN
    }
}
