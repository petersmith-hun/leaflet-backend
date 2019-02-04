package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * VO for contact request processing.
 *
 * @author Peter Smith
 */
public class ContactRequestVO {

    private String name;
    private String email;
    private String message;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContactRequestVO that = (ContactRequestVO) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(email, that.email)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(email)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("email", email)
                .append("message", message)
                .toString();
    }

    public static ContactRequestVOBuilder getBuilder() {
        return new ContactRequestVOBuilder();
    }

    /**
     * Builder for {@link ContactRequestVO}.
     */
    public static final class ContactRequestVOBuilder {
        private String name;
        private String email;
        private String message;

        private ContactRequestVOBuilder() {
        }

        public ContactRequestVOBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ContactRequestVOBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ContactRequestVOBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ContactRequestVO build() {
            ContactRequestVO contactRequestVO = new ContactRequestVO();
            contactRequestVO.name = this.name;
            contactRequestVO.email = this.email;
            contactRequestVO.message = this.message;
            return contactRequestVO;
        }
    }
}
