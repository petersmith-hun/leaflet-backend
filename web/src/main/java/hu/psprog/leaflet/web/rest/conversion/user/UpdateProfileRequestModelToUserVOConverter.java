package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.JULocaleToLeafletLocaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link UpdateProfileRequestModel} model to {@link UserVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class UpdateProfileRequestModelToUserVOConverter implements Converter<UpdateProfileRequestModel, UserVO> {

    @Autowired
    private JULocaleToLeafletLocaleConverter juLocaleToLeafletLocaleConverter;

    @Override
    public UserVO convert(UpdateProfileRequestModel updateProfileRequestModel) {

        return UserVO.getBuilder()
                .withUsername(updateProfileRequestModel.getUsername())
                .withEmail(updateProfileRequestModel.getEmail())
                .withLocale(juLocaleToLeafletLocaleConverter.convert(updateProfileRequestModel.getDefaultLocale()))
                .build();
    }
}
