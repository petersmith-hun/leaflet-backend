package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link EntryVO} value objects to {@link EntryListDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEntryDataModelListConverter implements Converter<List<EntryVO>, EntryListDataModel> {

    private EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter;

    @Autowired
    public EntryVOToEntryDataModelListConverter(EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter) {
        this.entryVOToEntryDataModelEntityConverter = entryVOToEntryDataModelEntityConverter;
    }

    @Override
    public EntryListDataModel convert(List<EntryVO> entryVOList) {

        EntryListDataModel.EntryListDataModelBuilder builder = EntryListDataModel.getBuilder();
        entryVOList.forEach(entryVO -> builder.withItem(entryVOToEntryDataModelEntityConverter.convert(entryVO)));

        return builder.build();
    }
}
