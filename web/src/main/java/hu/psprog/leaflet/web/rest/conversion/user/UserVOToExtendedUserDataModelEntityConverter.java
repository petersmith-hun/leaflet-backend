package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link UserVO} value object to {@link ExtendedUserDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class UserVOToExtendedUserDataModelEntityConverter implements Converter<UserVO, ExtendedUserDataModel> {

    private final DateConverter dateConverter;

    @Autowired
    public UserVOToExtendedUserDataModelEntityConverter(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public ExtendedUserDataModel convert(UserVO userVO) {

        return convertEntity(userVO);
    }

    private ExtendedUserDataModel convertEntity(UserVO userVO) {

        return ExtendedUserDataModel.getBuilder()
                .withLocale(userVO.getLocale().name())
                .withRole(extractRole(userVO))
                .withCreated(dateConverter.convert(userVO.getCreated()))
                .withLastLogin(dateConverter.convert(userVO.getLastLogin()))
                .withEmail(userVO.getEmail())
                .withLastModified(dateConverter.convert(userVO.getLastModified()))
                .withUsername(userVO.getUsername())
                .withId(userVO.getId())
                .build();
    }

    protected String extractRole(UserVO userVO) {

        return userVO.getAuthorities().toArray()[0].toString();
    }
}
