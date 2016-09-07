package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
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
        builder.withCreated(source.getCreated())
                .isEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withStatus(EntryStatus.valueOf(source.getEntryStatus()))
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withPrologue(source.getPrologue())
                .withTitle(source.getTitle())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (source.getOwner() != null) {
            builder.withUser(userVOToUserConverter.convert(source.getOwner()));
        }

        return builder.createEntry();
    }
}
