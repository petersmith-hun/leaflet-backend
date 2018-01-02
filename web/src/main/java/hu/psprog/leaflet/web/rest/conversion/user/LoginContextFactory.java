package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static hu.psprog.leaflet.security.jwt.filter.JWTAuthenticationFilter.DEVICE_ID_HEADER;

/**
 * Builds proper {@link LoginContextVO} object for processes.
 *
 * @author Peter Smith
 */
@Component
public class LoginContextFactory {

    /**
     * Builds {@link LoginContextVO} object for login process.
     * Requires:
     *  - username (email address) (from {@link LoginRequestModel})
     *  - password (from {@link LoginRequestModel})
     *  - device ID (from {@link HttpServletRequest})
     *  - remote address (from {@link HttpServletRequest})
     *
     * @param loginRequestModel {@link LoginRequestModel} holding username and password
     * @param request {@link HttpServletRequest} to extract device ID and remote address
     * @return LoginContextVO
     */
    public LoginContextVO forLogin(LoginRequestModel loginRequestModel, HttpServletRequest request) {
        return buildLoginContext(loginRequestModel.getEmail(), loginRequestModel.getPassword(), request);
    }

    /**
     * Builds {@link LoginContextVO} object for password reset process.
     * Requires:
     *  - username (email address) (from {@link PasswordResetDemandRequestModel})
     *  - device ID (from {@link HttpServletRequest})
     *  - remote address (from {@link HttpServletRequest})
     *
     * @param passwordResetDemandRequestModel {@link PasswordResetDemandRequestModel} holding username
     * @param request {@link HttpServletRequest} to extract device ID and remote address
     * @return LoginContextVO
     */
    public LoginContextVO forPasswordReset(PasswordResetDemandRequestModel passwordResetDemandRequestModel, HttpServletRequest request) {
        return buildLoginContext(passwordResetDemandRequestModel.getEmail(), null, request);
    }

    /**
     * Builds {@link LoginContextVO} object for session renewal process.
     * Requires:
     *  - device ID (from {@link HttpServletRequest})
     *  - remote address (from {@link HttpServletRequest})
     *
     * @param request {@link HttpServletRequest} to extract device ID and remote address
     * @return LoginContextVO
     */
    public LoginContextVO forRenewal(HttpServletRequest request) {
        return buildLoginContext(null, null, request);
    }

    private LoginContextVO buildLoginContext(String username, String password, HttpServletRequest request) {
        return LoginContextVO.getBuilder()
                .withUsername(username)
                .withPassword(password)
                .withRemoteAddress(request.getRemoteAddr())
                .withDeviceID(extractDeviceID(request))
                .build();
    }

    private UUID extractDeviceID(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(DEVICE_ID_HEADER))
                .map(UUID::fromString)
                .orElse(null);
    }
}
