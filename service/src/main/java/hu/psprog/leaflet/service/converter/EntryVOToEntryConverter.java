package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link EntryVO} to {@link Entry} object.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEntryConverter implements Converter<EntryVO, Entry> {

    @Autowired
    private UserVOToUserConverter userVOToUserConverter;

    @Override
    public Entry convert(EntryVO source) {

        Entry.Builder builder = new Entry.Builder();
        builder.withContent(source.getContent())
                .withRawContent(source.getRawContent())
                .withCreated(source.getCreated())
                .isEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withStatus(EntryStatus.valueOf(source.getEntryStatus()))
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withPrologue(source.getPrologue())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (source.getOwner() != null) {
            User user = new User.Builder()
                    .withId(source.getOwner().getId())
                    .createUser();
            builder.withUser(user);
        }

        return builder.createEntry();
    }
}
