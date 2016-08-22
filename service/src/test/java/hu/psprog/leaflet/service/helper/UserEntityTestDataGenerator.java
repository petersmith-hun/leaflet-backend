package hu.psprog.leaflet.service.helper;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Random test {@link User} entity generator.
 *
 * @author Peter Smith
 */
public class UserEntityTestDataGenerator implements TestDataGenerator<User> {

    @Override
    public User generate() {
        return new User.Builder()
                .withRole(Role.USER)
                .withEmail(RandomDataGenerator.email())
                .withCreated(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .withLastModified(RandomDataGenerator.randomDate(RandomDataGenerator.DateType.PAST))
                .isEnabled(true)
                .withDefaultLocale(Locale.EN)
                .withPassword(RandomDataGenerator.randomString(12))
                .withId(RandomDataGenerator.randomLong())
                .withUsername(RandomDataGenerator.sequentialString())
                .createUser();
    }

    @Override
    public List<User> generate(int count) {

        List<User> users = new LinkedList<>();
        for (int index = 0; index < count; index++) {
            users.add(generate());
        }

        return null;
    }
}
