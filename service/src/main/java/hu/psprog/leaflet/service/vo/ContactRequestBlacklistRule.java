package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.service.exception.InvalidBlacklistRule;
import lombok.Data;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Domain class modelling a contact request blacklist rule.
 * Blacklist rules must contain the following parts:
 *  - object: can be 'name', 'message' or 'email' respectively for matching the name, message or email parameter of the given contact request;
 *  - operator: can be 'equals' (exact match) or 'contains' (partial match);
 *  - and the value to check the given parameter against (inbetween apostrophes).
 *
 * Examples of valid rules:
 *  - name equals 'Test User'
 *  - message contains 'something bad'
 *  - email equals 'spams@dev.local'
 *
 * @author Peter Smith
 */
@Data
public class ContactRequestBlacklistRule {

    private static final Pattern RULE_PATTERN = Pattern.compile("(name|message|email) (equals|contains) '(.*)'", Pattern.CASE_INSENSITIVE);

    private final BlacklistObject object;
    private final BlacklistOperator operator;
    private final String value;

    private ContactRequestBlacklistRule(BlacklistObject object, BlacklistOperator operator, String value) {
        this.object = object;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Checks if the given contact request matches against this blacklist rule.
     * In case the request matches the rule, the request should be rejected.
     *
     * @param contactRequestVO {@link ContactRequestVO} object containing the request information to be checked
     * @return {@code true} if the rule matches the request (and therefore should be rejected), {@code false} otherwise
     */
    public boolean match(ContactRequestVO contactRequestVO) {

        String extractedValue = object.valueExtractionFunction.apply(contactRequestVO);

        return operator.valueMatchFunction.apply(extractedValue, value);
    }

    /**
     * Parses the given blacklist rule provided as a string value.
     *
     * @param rule blacklist rule as string (see class Javadoc for details)
     * @return parsed blacklist rule as {@link ContactRequestBlacklistRule} object
     */
    public static ContactRequestBlacklistRule parseRule(String rule) {

        Matcher ruleMatcher = RULE_PATTERN.matcher(rule);

        if (!ruleMatcher.matches()) {
            throw new InvalidBlacklistRule(rule);
        }

        if (ruleMatcher.groupCount() != 3) {
            throw new InvalidBlacklistRule(ruleMatcher.groupCount());
        }

        return new ContactRequestBlacklistRule(
                BlacklistObject.valueOf(ruleMatcher.group(1).toUpperCase()),
                BlacklistOperator.valueOf(ruleMatcher.group(2).toUpperCase()),
                ruleMatcher.group(3)
        );
    }

    @Override
    public String toString() {

        return String.format("Blacklist rule: %s object must not %s value '%s'",
                object.name().toLowerCase(), operator.summary, value);
    }

    /**
     * Supported contact request parameter types.
     */
    public enum BlacklistObject {

        NAME(ContactRequestVO::getName),
        MESSAGE(ContactRequestVO::getMessage),
        EMAIL(ContactRequestVO::getEmail);

        private final Function<ContactRequestVO, String> valueExtractionFunction;

        BlacklistObject(Function<ContactRequestVO, String> valueExtractionFunction) {
            this.valueExtractionFunction = valueExtractionFunction;
        }
    }

    /**
     * Supported blacklisting operators.
     */
    public enum BlacklistOperator {

        EQUALS(String::equalsIgnoreCase, "be equal to"),
        CONTAINS(String::contains, "contain");

        private final BiFunction<String, String, Boolean> valueMatchFunction;
        private final String summary;

        BlacklistOperator(BiFunction<String, String, Boolean> valueMatchFunction, String summary) {
            this.valueMatchFunction = valueMatchFunction;
            this.summary = summary;
        }
    }
}
