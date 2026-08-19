package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * VO for {@link User} entity.
 *
 * @author Peter Smith
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class UserVO extends IdentifiableVO<Long> {

    @Getter
    public enum OrderBy {
        ID("id"),
        USERNAME("username"),
        EMAIL("email"),
        CREATED("created");

        private final String field;

        OrderBy(String field) {
            this.field = field;
        }

    }

    private final String username;
    private final String email;

    public static UserVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
