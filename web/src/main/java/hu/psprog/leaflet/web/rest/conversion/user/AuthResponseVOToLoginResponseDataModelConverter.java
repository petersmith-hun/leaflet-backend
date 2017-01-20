package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.service.vo.AuthResponseVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link AuthResponseVO} value object to
 *
 * @author Peter Smith
 */
@Component
public class AuthResponseVOToLoginResponseDataModelConverter implements Converter<AuthResponseVO, LoginResponseDataModel> {

    @Override
    public LoginResponseDataModel convert(AuthResponseVO authResponseVO) {

        LoginResponseDataModel.AuthenticationResult authenticationResult =
                LoginResponseDataModel.AuthenticationResult.valueOf(authResponseVO.getAuthenticationResult().name());
        
        LoginResponseDataModel.Builder builder = new LoginResponseDataModel.Builder()
                .withStatus(authenticationResult);

        if (authResponseVO.getAuthenticationResult() == AuthResponseVO.AuthenticationResult.AUTH_SUCCESS) {
            builder.withToken(authResponseVO.getToken());
        }

        return builder.build();
    }
}
