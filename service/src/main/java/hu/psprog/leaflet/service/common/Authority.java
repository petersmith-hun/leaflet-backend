package hu.psprog.leaflet.service.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Known authority types.
 *
 * @author Peter Smith
 */
public interface Authority {

    enum AuthorityName {
        ADMIN,
        USER,
        EDITOR,
        SERVICE
    }

    GrantedAuthority ADMIN = new SimpleGrantedAuthority(AuthorityName.ADMIN.name());

    GrantedAuthority USER = new SimpleGrantedAuthority(AuthorityName.USER.name());

    GrantedAuthority EDITOR = new SimpleGrantedAuthority(AuthorityName.EDITOR.name());

    GrantedAuthority SERVICE = new SimpleGrantedAuthority(AuthorityName.SERVICE.name());

    static GrantedAuthority getAuthorityByName(String authorityName) {

        switch (Authority.AuthorityName.valueOf(authorityName)) {
            case ADMIN:
                return Authority.ADMIN;
            case USER:
                return Authority.USER;
            case EDITOR:
                return Authority.EDITOR;
            case SERVICE:
                return Authority.SERVICE;
            default:
                return Authority.USER;
        }
    }
}
