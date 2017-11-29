package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.JULocaleToLeafletLocaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Converts {@link UserCreateRequestModel} model to {@link UserVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class UserInitializeRequestModelToUserVOConverter implements Converter<UserInitializeRequestModel, UserVO> {

    private static final String USERS_ENABLED_BY_DEFAULT = "${usersEnabledByDefault:true}";

    private boolean enabled;
    private JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter;

    @Autowired
    public UserInitializeRequestModelToUserVOConverter(@Value(USERS_ENABLED_BY_DEFAULT) boolean enabled,
                                                       JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter) {
        this.enabled = enabled;
        this.juLocaleToLeafletLocaleConverter = juLocaleToLeafletLocaleConverter;
    }

    @Override
    public UserVO convert(UserInitializeRequestModel userInitializeRequestModel) {

        return UserVO.getBuilder()
                .withUsername(userInitializeRequestModel.getUsername())
                .withEmail(userInitializeRequestModel.getEmail())
                .withPassword(userInitializeRequestModel.getPassword())
                .withEnabled(enabled)
                .withCreated(new Date())
                .withLocale(juLocaleToLeafletLocaleConverter.convert(userInitializeRequestModel.getDefaultLocale()))
                .withAuthorities(extractAuthorities(userInitializeRequestModel))
                .build();
    }

    private List<GrantedAuthority> extractAuthorities(UserInitializeRequestModel userInitializeRequestModel) {

        GrantedAuthority authority = Authority.USER;
        if (userInitializeRequestModel instanceof UserCreateRequestModel) {
            String role = ((UserCreateRequestModel) userInitializeRequestModel).getRole();
            authority = Authority.getAuthorityByName(role);
        }

        return Collections.singletonList(authority);
    }
}
