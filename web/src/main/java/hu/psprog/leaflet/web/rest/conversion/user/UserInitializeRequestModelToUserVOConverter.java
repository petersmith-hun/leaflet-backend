package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.config.ConfigurationProperty;
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

    private boolean enabled;
    private RunLevel runLevel;
    private JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter;

    @Autowired
    public UserInitializeRequestModelToUserVOConverter(@Value(ConfigurationProperty.USERS_ENABLED_BY_DEFAULT) boolean enabled,
                                                       RunLevel runLevel, JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter) {
        this.enabled = enabled;
        this.runLevel = runLevel;
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
        } else if (runLevel == RunLevel.INIT) {
            authority = Authority.ADMIN;
        }

        return Collections.singletonList(authority);
    }
}
