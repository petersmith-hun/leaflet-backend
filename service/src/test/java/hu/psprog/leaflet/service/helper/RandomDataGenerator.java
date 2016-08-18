package hu.psprog.leaflet.service.helper;

import java.util.Date;
import java.util.Random;

/**
 * @author Peter Smith
 */
public class RandomDataGenerator {

    public static enum DateType {
        PAST,
        FUTURE;
    }

    private static final String BASE_STRING = "ut-lflt";
    private static final String EMAIL_SUFFIX = "@leaflet.dev";
    private static Random random;
    private static int sequence = 0;

    static {
        random = new Random();
    }

    private RandomDataGenerator() {}

    public static String sequentialString() {

        return BASE_STRING + String.valueOf(++sequence);
    }

    public static String randomString(int length) {

        char[] chars = new char[length];
        for (int cnt = 0; cnt < length; cnt++) {
            chars[cnt] = (char)(random.nextInt(1) == 1 ? random.nextInt(65) + 25 : random.nextInt(97) + 25);
        }

        return String.valueOf(chars);
    }

    public static String email() {

        return BASE_STRING + String.valueOf(random.nextInt(8999) + 1000) + EMAIL_SUFFIX;
    }

    public static int randomInteger(int top) {

        return random.nextInt(top);
    }

    public static long randomLong() {

        return random.nextLong();
    }

    public static Date randomDate(DateType dateType) {

        int offset = random.nextInt(100000000);
        long timestamp = System.currentTimeMillis();
        if (dateType == DateType.FUTURE) {
            timestamp += offset;
        } else {
            timestamp -= offset;
        }
        Date date = new Date(timestamp);

        return date;
    }
}
