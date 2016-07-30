package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.SafeUserVO;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a {@link User} object {@link SafeUserVO} object.
 *
 * @author Peter Smith
 */
public class UserToUserVOConverter implements Converter<User, SafeUserVO> {

    @Override
    public SafeUserVO convert(User source) {
        return null;
    }
}
