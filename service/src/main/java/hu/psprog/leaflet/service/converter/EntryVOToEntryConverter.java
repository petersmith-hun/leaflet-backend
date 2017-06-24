package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts {@link EntryVO} to {@link Entry} object.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEntryConverter implements Converter<EntryVO, Entry> {

    @Override
    public Entry convert(EntryVO source) {

        Entry.EntryBuilder builder = Entry.getBuilder();
        builder.withContent(source.getContent())
                .withRawContent(source.getRawContent())
                .withCreated(source.getCreated())
                .withEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withPrologue(source.getPrologue())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (Objects.nonNull(source.getEntryStatus())) {
            builder.withStatus(EntryStatus.valueOf(source.getEntryStatus()));
        }

        if (Objects.nonNull(source.getCategory())) {
            Category category = Category.getBuilder()
                    .withId(source.getCategory().getId())
                    .build();
            builder.withCategory(category);
        }

        if (source.getOwner() != null) {
            User user = User.getBuilder()
                    .withId(source.getOwner().getId())
                    .build();
            builder.withUser(user);
        }

        return builder.build();
    }
}
