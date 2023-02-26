package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Locale;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * Value object for {@link Entry} entity.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
public class EntryVO extends SelfStatusAwareIdentifiableVO<Long, Entry> implements CustomSEODataProviderVO {

    public enum OrderBy {
        ID("id"),
        TITLE("title"),
        CREATED("created"),
        PUBLISHED("published");

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
    private final String prologue;
    private final String rawContent;
    private final String seoTitle;
    private final String seoDescription;
    private final String seoKeywords;
    private final String entryStatus;
    private final UserVO owner;
    private final CategoryVO category;
    private final Locale locale;
    private final List<UploadedFileVO> attachments;
    private final List<TagVO> tags;
    private final Date published;

    public static EntryVO wrapMinimumVO(Long id) {

        return getBuilder()
                .withId(id)
                .build();
    }
}
