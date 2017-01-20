package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
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
public class UserVOToExtendedUserDataModelListConverter implements Converter<List<UserVO>, UserListDataModel> {

    @Autowired
    private UserVOToExtendedUserDataModelEntityConverter userVOToExtendedUserDataModelEntityConverter;

    @Override
    public UserListDataModel convert(List<UserVO> userVOList) {

        UserListDataModel.Builder builder = new UserListDataModel.Builder();
        userVOList.forEach(userVO -> builder.withItem(userVOToExtendedUserDataModelEntityConverter.convert(userVO)));

        return builder.build();
    }
}
