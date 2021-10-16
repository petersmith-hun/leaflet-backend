package hu.psprog.leaflet.service.helper;

import java.util.Date;
import java.util.Random;

/**
 * Random data generator for unit tests.
 *
 * @author Peter Smith
 */
public class RandomDataGenerator {

    /**
     * Date types.
     */
    public enum DateType {

        /**
         * Generated date is in the past.
         */
        PAST,

        /**
         * Generated date is in the future.
         */
        FUTURE
    }

    private static final String BASE_STRING = "ut-lflt";
    private static final String EMAIL_SUFFIX = "@leaflet.dev";
    private static final Random random;
    private static int sequence = 0;

    static {
        random = new Random();
    }

    private RandomDataGenerator() {}

    /**
     * String built from a base string and a sequential number.
     *
     * @return sequential string
     */
    public static String sequentialString() {

        return BASE_STRING + ++sequence;
    }

    /**
     * Random string of length {@code length}
     *
     * @param length length of random string
     * @return random string
     */
    public static String randomString(int length) {

        char[] chars = new char[length];
        for (int cnt = 0; cnt < length; cnt++) {
            chars[cnt] = (char)(random.nextInt(2) == 1
                    ? random.nextInt(65) + 25
                    : random.nextInt(97) + 25);
        }

        return String.valueOf(chars);
    }

    /**
     * Email-like string built from a base string, a random number (1000-9999) and an email suffix.
     *
     * @return email-like string
     */
    public static String email() {

        return BASE_STRING + (random.nextInt(8999) + 1000) + EMAIL_SUFFIX;
    }

    /**
     * Random long number.
     *
     * @return random long
     */
    public static long randomLong() {

        return random.nextLong();
    }

    /**
     * Random date based on {@code dateType}. See documentation of {@link DateType}.
     *
     * @param dateType date type (past/future)
     * @return random date
     */
    public static Date randomDate(DateType dateType) {

        int offset = random.nextInt(100000000);
        long timestamp = System.currentTimeMillis();
        if (dateType == DateType.FUTURE) {
            timestamp += offset;
        } else {
            timestamp -= offset;
        }

        return new Date(timestamp);
    }
}
