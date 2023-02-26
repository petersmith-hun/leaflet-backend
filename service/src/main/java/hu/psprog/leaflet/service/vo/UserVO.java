package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * VO for {@link User} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class UserVO extends SelfStatusAwareIdentifiableVO<Long, User> {

    public enum OrderBy {
        ID("id"),
        USERNAME("username"),
        EMAIL("email"),
        CREATED("created");

        private final String field;

        OrderBy(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    private final String username;
    private final String email;
    private final Collection<GrantedAuthority> authorities;

    @ToStringExclude
    private final String password;
    private final Locale locale;
    private final Date lastLogin;

    public static UserVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
