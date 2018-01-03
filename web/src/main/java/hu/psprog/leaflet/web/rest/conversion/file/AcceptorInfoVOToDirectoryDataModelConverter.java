package hu.psprog.leaflet.web.rest.conversion.file;

import hu.psprog.leaflet.api.rest.response.file.DirectoryDataModel;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link AcceptorInfoVO} to {@link DirectoryDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class AcceptorInfoVOToDirectoryDataModelConverter implements Converter<AcceptorInfoVO, DirectoryDataModel> {

    @Override
    public DirectoryDataModel convert(AcceptorInfoVO source) {

        return DirectoryDataModel.getBuilder()
                .withId(source.getId())
                .withRoot(source.getRootDirectoryName())
                .withChildren(source.getChildrenDirectories())
                .withAcceptableMimeTypes(source.getAcceptableMimeTypes())
                .build();
    }
}
