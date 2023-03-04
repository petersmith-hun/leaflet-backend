package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * VO for {@link Category} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class CategoryVO extends SelfStatusAwareIdentifiableVO<Long, Category> {

    private final String title;
    private final String description;

    public static CategoryVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
