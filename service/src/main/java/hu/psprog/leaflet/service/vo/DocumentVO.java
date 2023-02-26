package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.Locale;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * VO for {@link Document} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class DocumentVO extends SelfStatusAwareIdentifiableVO<Long, Document> implements CustomSEODataProviderVO {

    public enum OrderBy {
        ID("id"),
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
    private final String link;
    private final String rawContent;
    private final String seoTitle;
    private final String seoDescription;
    private final String seoKeywords;
    private final UserVO owner;
    private final Locale locale;

    public static DocumentVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
