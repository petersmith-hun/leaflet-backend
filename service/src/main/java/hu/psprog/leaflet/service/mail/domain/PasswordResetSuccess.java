package hu.psprog.leaflet.service.mail.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain object holding information for successful password reset notification mail.
 *
 * @author Peter Smith
 */
public class PasswordResetSuccess {

    private String participant;
    private String username;

    public String getParticipant() {
        return participant;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PasswordResetSuccess)) return false;

        PasswordResetSuccess that = (PasswordResetSuccess) o;

        return new EqualsBuilder()
                .append(participant, that.participant)
                .append(username, that.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(participant)
                .append(username)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("participant", participant)
                .append("username", username)
                .toString();
    }

    public static PasswordResetSuccessBuilder getBuilder() {
        return new PasswordResetSuccessBuilder();
    }

    /**
     * Builder for {@link PasswordResetSuccess}.
     */
    public static final class PasswordResetSuccessBuilder {
        private String participant;
        private String username;

        private PasswordResetSuccessBuilder() {
        }

        public PasswordResetSuccessBuilder withParticipant(String participant) {
            this.participant = participant;
            return this;
        }

        public PasswordResetSuccessBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public PasswordResetSuccess build() {
            PasswordResetSuccess passwordResetSuccess = new PasswordResetSuccess();
            passwordResetSuccess.participant = this.participant;
            passwordResetSuccess.username = this.username;
            return passwordResetSuccess;
        }
    }
}
