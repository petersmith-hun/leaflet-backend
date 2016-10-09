package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Entry} to {@link EntryVO} object.
 *
 * @author Peter Smith
 */
@Component
public class EntryToEntryVOConverter implements Converter<Entry, EntryVO> {

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    @Override
    public EntryVO convert(Entry source) {

        EntryVO.Builder builder = new EntryVO.Builder();
        builder.withContent(source.getContent())
                .withCreated(source.getCreated())
                .withEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withEntryStatus(source.getStatus().name())
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withPrologue(source.getPrologue())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle());

        if (source.getUser() != null) {
            builder.withOwner(userToUserVOConverter.convert(source.getUser()));
        }

        return builder.createEntryVO();
    }
}
