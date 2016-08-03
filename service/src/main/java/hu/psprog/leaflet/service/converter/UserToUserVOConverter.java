package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Converts a {@link User} object {@link UserVO} object.
 *
 * @author Peter Smith
 */
@Component
public class UserToUserVOConverter implements Converter<User, UserVO> {

    @Autowired
    private RoleToAuthorityConverter roleToAuthorityConverter;

    @Override
    public UserVO convert(User source) {

        List<GrantedAuthority> authorities = Arrays.asList(roleToAuthorityConverter.convert(source.getRole()));

        return new UserVO.Builder()
                .withAuthorities(authorities)
                .withCreated(source.getCreated())
                .withEmail(source.getEmail())
                .withEnabled(source.isEnabled())
                .withId(source.getId())
                .withLastModified(source.getLastModified())
                .withLocale(source.getDefaultLocale())
                .withPassword(source.getPassword())
                .withUsername(source.getUsername())
                .createSafeUserVO();
    }
}
