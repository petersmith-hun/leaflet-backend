package hu.psprog.leaflet.service.helper;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Random test {@link UserVO} entity generator.
 *
 * @author Peter Smith
 */
public class UserVOTestDataGenerator implements TestDataGenerator<UserVO> {

    @Override
    public UserVO generate() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return UserVO.getBuilder()
                .withAuthorities(authorities)
                .withEmail(RandomDataGenerator.email())
                .withCreated(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .withLastModified(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .withEnabled(true)
                .withLocale(Locale.EN)
                .withPassword(RandomDataGenerator.randomString(12))
                .withId(RandomDataGenerator.randomLong())
                .withUsername(RandomDataGenerator.sequentialString())
                .build();
    }

}
