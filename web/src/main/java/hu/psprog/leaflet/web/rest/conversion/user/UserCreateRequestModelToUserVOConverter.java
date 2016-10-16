package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.config.ConfigurationProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * Converts {@link UserCreateRequestModel} model to {@link UserVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class UserCreateRequestModelToUserVOConverter implements Converter<UserCreateRequestModel, UserVO> {

    @Value(ConfigurationProperty.USERS_ENABLED_BY_DEFAULT)
    private boolean enabled;

    @Override
    public UserVO convert(UserCreateRequestModel userCreateRequestModel) {

        return new UserVO.Builder()
                .withUsername(userCreateRequestModel.getUsername())
                .withEmail(userCreateRequestModel.getEmail())
                .withPassword(userCreateRequestModel.getPassword())
                .withEnabled(enabled)
                .withCreated(new Date())
                .withLocale(extractKnownLocale(userCreateRequestModel.getDefaultLocale()))
                .withAuthorities(Arrays.asList(Authority.getAuthorityByName(userCreateRequestModel.getRole())))
                .createUserVO();
    }

    private Locale extractKnownLocale(java.util.Locale originalLocale) {

        switch (originalLocale.getLanguage()) {
            case "hu":
            case "hu_HU":
                return Locale.HU;
            default:
                return Locale.EN;
        }
    }
}
