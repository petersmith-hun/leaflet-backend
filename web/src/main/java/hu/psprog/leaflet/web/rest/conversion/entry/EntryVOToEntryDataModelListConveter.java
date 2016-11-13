package hu.psprog.leaflet.web.rest.conversion.entry;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.layout.DefaultListLayoutDataModel;
import hu.psprog.leaflet.service.vo.EntryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link EntryVO} value objects to {@link DefaultListLayoutDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class EntryVOToEntryDataModelListConveter implements Converter<List<EntryVO>, BaseBodyDataModel> {

    private static final String LIST_NODE_NAME = "entries";

    @Autowired
    private EntryVOToEntryDataModelEntityConverter entryVOToEntryDataModelEntityConverter;

    @Override
    public BaseBodyDataModel convert(List<EntryVO> entryVOList) {

        DefaultListLayoutDataModel.Builder builder = new DefaultListLayoutDataModel.Builder();
        builder.setNodeName(LIST_NODE_NAME);
        entryVOList.forEach(entryVO -> builder.withItem(entryVOToEntryDataModelEntityConverter.convert(entryVO)));

        return builder.build();
    }
}
