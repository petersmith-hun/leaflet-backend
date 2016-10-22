package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.service.vo.AuthRequestVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link LoginRequestModel} model to {@link AuthRequestVO} model.
 *
 * @author Peter Smith
 */
@Component
public class LoginRequestModelToAuthenticationRequestVOConverter implements Converter<LoginRequestModel, AuthRequestVO> {

    @Override
    public AuthRequestVO convert(LoginRequestModel loginRequestModel) {

        return new AuthRequestVO.Builder()
                .withUsername(loginRequestModel.getEmail())
                .withPassword(loginRequestModel.getPassword())
                .createAuthRequestVO();
    }
}
