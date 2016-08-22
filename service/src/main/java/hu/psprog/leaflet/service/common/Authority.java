package hu.psprog.leaflet.service.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Known authority types.
 *
 * @author Peter Smith
 */
public interface Authority {

    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ADMIN");

    public static final GrantedAuthority USER = new SimpleGrantedAuthority("USER");

    public static final GrantedAuthority EDITOR = new SimpleGrantedAuthority("EDITOR");

    public static final GrantedAuthority SERVICE = new SimpleGrantedAuthority("SERVICE");
}
