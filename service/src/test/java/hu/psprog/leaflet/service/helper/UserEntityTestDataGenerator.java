package hu.psprog.leaflet.service.helper;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;

/**
 * Random test {@link User} entity generator.
 *
 * @author Peter Smith
 */
public class UserEntityTestDataGenerator implements TestDataGenerator<User> {

    @Override
    public User generate() {

        return User.getBuilder()
                .withRole(Role.USER)
                .withEmail(RandomDataGenerator.email())
                .withCreated(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .withLastModified(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .withEnabled(true)
                .withDefaultLocale(Locale.EN)
                .withPassword(RandomDataGenerator.randomString(12))
                .withId(RandomDataGenerator.randomLong())
                .withUsername(RandomDataGenerator.sequentialString())
                .build();
    }

}
