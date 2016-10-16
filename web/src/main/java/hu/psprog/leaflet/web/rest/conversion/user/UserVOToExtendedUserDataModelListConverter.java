package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entity.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.layout.DefaultListLayoutDataModel;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link UserVO} value objects to {@link ExtendedUserDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class UserVOToExtendedUserDataModelListConverter implements Converter<List<UserVO>, BaseBodyDataModel> {

    private static final String LIST_NODE_NAME = "users";

    @Autowired
    private UserVOToExtendedUserDataModelEntityConverter userVOToExtendedUserDataModelEntityConverter;

    @Override
    public BaseBodyDataModel convert(List<UserVO> userVOList) {

        DefaultListLayoutDataModel.Builder responseBuilder = new DefaultListLayoutDataModel.Builder();
        responseBuilder.setNodeName(LIST_NODE_NAME);
        userVOList.forEach(userVO -> responseBuilder.withItem(userVOToExtendedUserDataModelEntityConverter.convert(userVO)));

        return responseBuilder.build();
    }
}
