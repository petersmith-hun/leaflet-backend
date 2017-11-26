package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link AcceptorInfoVO} objects to {@link DirectoryListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class AcceptorInfoVOListToDirectoryListDataModelConverter implements Converter<List<AcceptorInfoVO>, DirectoryListDataModel> {

    private AcceptorInfoVOToDirectoryDataModelConverter acceptorInfoVOToDirectoryDataModelConverter;

    @Autowired
    public AcceptorInfoVOListToDirectoryListDataModelConverter(AcceptorInfoVOToDirectoryDataModelConverter acceptorInfoVOToDirectoryDataModelConverter) {
        this.acceptorInfoVOToDirectoryDataModelConverter = acceptorInfoVOToDirectoryDataModelConverter;
    }

    @Override
    public DirectoryListDataModel convert(List<AcceptorInfoVO> source) {

        DirectoryListDataModel.DirectoryListDataModelBuilder builder = DirectoryListDataModel.getBuilder();
        source.stream()
                .map(acceptorInfoVOToDirectoryDataModelConverter::convert)
                .forEach(builder::withItem);

        return builder.build();
    }
}
