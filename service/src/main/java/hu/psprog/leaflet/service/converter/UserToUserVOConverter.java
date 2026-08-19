package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link User} object {@link UserVO} object.
 *
 * @author Peter Smith
 */
@Component
public class UserToUserVOConverter implements Converter<User, UserVO> {

    @Override
    public UserVO convert(User source) {

        return UserVO.getBuilder()
                .withEmail(source.getEmail())
                .withId(source.getId())
                .withUsername(source.getUsername())
                .build();
    }
}
