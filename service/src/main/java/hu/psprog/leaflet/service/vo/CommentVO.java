package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Comment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * VO for {@link Comment} entity.
 *
 * @author Peter Smith
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class CommentVO extends LogicallyDeletableSelfStatusAwareIdentifiableVO<Long> {

    @Getter
    public enum OrderBy {
        ID("id"),
        CREATED("created");

        private final String field;

        OrderBy(String field) {
            this.field = field;
        }
    }

    private final UserVO owner;
    private final EntryVO entryVO;
    private final String content;

    public static CommentVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
