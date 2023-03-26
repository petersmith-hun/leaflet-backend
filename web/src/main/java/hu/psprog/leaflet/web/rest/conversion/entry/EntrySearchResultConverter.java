package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EntrySearchResultDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts a list of {@link EntryVO} objects to {@link EntrySearchResultDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class EntrySearchResultConverter implements Converter<List<EntryVO>, EntrySearchResultDataModel> {

    private final EntryVOToEditEntryDataModelEntityConverter entityConverter;

    @Autowired
    public EntrySearchResultConverter(EntryVOToEditEntryDataModelEntityConverter entityConverter) {
        this.entityConverter = entityConverter;
    }

    @Override
    public EntrySearchResultDataModel convert(List<EntryVO> source) {

        return EntrySearchResultDataModel.getBuilder()
                .withEntries(source.stream()
                        .map(entityConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
