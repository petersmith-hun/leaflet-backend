package hu.psprog.leaflet.service.vo;

import java.util.UUID;

/**
 * Login information holder.
 *
 * @author Peter Smith
 */
public class LoginContextVO {

    private String username;
    private String password;
    private UUID deviceID;
    private String remoteAddress;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getDeviceID() {
        return deviceID;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public static LoginContextVOBuilder getBuilder() {
        return new LoginContextVOBuilder();
    }

    /**
     * Builder for {@link LoginContextVO}.
     */
    public static final class LoginContextVOBuilder {
        private String username;
        private String password;
        private UUID deviceID;
        private String remoteAddress;

        private LoginContextVOBuilder() {
        }

        public LoginContextVOBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public LoginContextVOBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public LoginContextVOBuilder withDeviceID(UUID deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public LoginContextVOBuilder withRemoteAddress(String remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        public LoginContextVO build() {
            LoginContextVO loginContextVO = new LoginContextVO();
            loginContextVO.username = this.username;
            loginContextVO.remoteAddress = this.remoteAddress;
            loginContextVO.password = this.password;
            loginContextVO.deviceID = this.deviceID;
            return loginContextVO;
        }
    }
}
