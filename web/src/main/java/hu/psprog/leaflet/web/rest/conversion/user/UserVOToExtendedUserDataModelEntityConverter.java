package hu.psprog.leaflet.web.rest.conversion.user;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
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
public class UserVOToExtendedUserDataModelEntityConverter implements Converter<UserVO, ExtendedUserDataModel> {

    private CommonFormatter commonFormatter;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public UserVOToExtendedUserDataModelEntityConverter(CommonFormatter commonFormatter, HttpServletRequest httpServletRequest) {
        this.commonFormatter = commonFormatter;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ExtendedUserDataModel convert(UserVO userVO) {

        return convertEntity(userVO);
    }

    private ExtendedUserDataModel convertEntity(UserVO userVO) {

        return ExtendedUserDataModel.getExtendedBuilder()
                .withLocale(userVO.getLocale().name())
                .withRole(extractRole(userVO))
                .withCreated(commonFormatter.formatDate(userVO.getCreated(), httpServletRequest.getLocale()))
                .withLastLogin(commonFormatter.formatDate(userVO.getLastLogin(), httpServletRequest.getLocale()))
                .withEmail(userVO.getEmail())
                .withLastModified(commonFormatter.formatDate(userVO.getLastModified(), httpServletRequest.getLocale()))
                .withUsername(userVO.getUsername())
                .withId(userVO.getId())
                .build();
    }

    protected String extractRole(UserVO userVO) {

        return userVO.getAuthorities().toArray()[0].toString();
    }
}
