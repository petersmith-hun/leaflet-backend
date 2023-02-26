package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * VO for {@link Tag} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class TagVO extends SelfStatusAwareIdentifiableVO<Long, Tag> {

    public enum OrderBy {
        ID("id"),
        TITLE("title"),
        CREATED("created");

        private final String field;

        OrderBy(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    private final String title;

    public static TagVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
