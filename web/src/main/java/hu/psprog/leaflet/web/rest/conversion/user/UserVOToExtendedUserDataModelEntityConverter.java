package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entity.ExtendedUserDataModel;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link UserVO} value object to {@link ExtendedUserDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class UserVOToExtendedUserDataModelEntityConverter implements Converter<UserVO, BaseBodyDataModel> {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public BaseBodyDataModel convert(UserVO userVO) {

        return convertEntity(userVO);
    }

    private BaseBodyDataModel convertEntity(UserVO userVO) {

        return new ExtendedUserDataModel.Builder()
                .withLocale(userVO.getLocale().name())
                .withRole(extractRole(userVO))
                .withCreated(commonFormatter.formatDate(userVO.getCreated(), httpServletRequest.getLocale()))
                .withLastLogin(commonFormatter.formatDate(userVO.getLastLogin(), httpServletRequest.getLocale()))
                .withEmail(userVO.getEmail())
                .withUsername(userVO.getUsername())
                .withID(userVO.getId())
                .build();
    }

    protected String extractRole(UserVO userVO) {

        return userVO.getAuthorities().toArray()[0].toString();
    }
}
