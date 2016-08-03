package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class EntryToEntryVOConverter implements Converter<Entry, EntryVO> {

    @Override
    public EntryVO convert(Entry source) {
        return null;
    }
}
