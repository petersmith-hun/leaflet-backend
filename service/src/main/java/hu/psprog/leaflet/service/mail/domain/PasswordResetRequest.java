package hu.psprog.leaflet.service.mail.domain;

import hu.psprog.leaflet.security.jwt.model.ExtendedUserDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain object holding password reset request information for reset mail.
 *
 * @author Peter Smith
 */
public class PasswordResetRequest {

    private String participant;
    private ExtendedUserDetails userDetails;
    private String token;
    private int expiration;

    public String getParticipant() {
        return participant;
    }

    public ExtendedUserDetails getUserDetails() {
        return userDetails;
    }

    public String getToken() {
        return token;
    }

    public int getExpiration() {
        return expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PasswordResetRequest)) return false;

        PasswordResetRequest that = (PasswordResetRequest) o;

        return new EqualsBuilder()
                .append(expiration, that.expiration)
                .append(participant, that.participant)
                .append(userDetails, that.userDetails)
                .append(token, that.token)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(participant)
                .append(userDetails)
                .append(token)
                .append(expiration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("participant", participant)
                .append("userDetails", userDetails)
                .append("token", token)
                .append("expiration", expiration)
                .toString();
    }

    public static PasswordResetRequestBuilder getBuilder() {
        return new PasswordResetRequestBuilder();
    }

    /**
     * Builder for {@link PasswordResetRequest}.
     */
    public static final class PasswordResetRequestBuilder {
        private String participant;
        private ExtendedUserDetails userDetails;
        private String token;
        private int expiration;

        private PasswordResetRequestBuilder() {
        }

        public PasswordResetRequestBuilder withParticipant(String participant) {
            this.participant = participant;
            return this;
        }

        public PasswordResetRequestBuilder withUserDetails(ExtendedUserDetails userDetails) {
            this.userDetails = userDetails;
            return this;
        }

        public PasswordResetRequestBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public PasswordResetRequestBuilder withExpiration(int expiration) {
            this.expiration = expiration;
            return this;
        }

        public PasswordResetRequest build() {
            PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
            passwordResetRequest.participant = this.participant;
            passwordResetRequest.token = this.token;
            passwordResetRequest.userDetails = this.userDetails;
            passwordResetRequest.expiration = this.expiration;
            return passwordResetRequest;
        }
    }
}
