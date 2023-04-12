package hu.psprog.leaflet.web.metrics;

import io.micrometer.core.annotation.Counted;
import org.springframework.stereotype.Component;

/**
 * Counter metrics for exception handler.
 *
 * @author Peter Smith
 */
@Component
public class ExceptionHandlerCounters {

    @Counted(value = "resourceNotFound", extraTags = {"counter", "apiError"})
    public void resourceNotFound() {
    }

    @Counted(value = "authenticationFailure", extraTags = {"counter", "apiError"})
    public void authenticationFailure() {
    }

    @Counted(value = "authorizationFailure", extraTags = {"counter", "apiError"})
    public void authorizationFailure() {
    }

    @Counted(value = "requestCouldNotBeFulfilled", extraTags = {"counter", "apiError"})
    public void requestCouldNotBeFulfilled() {
    }

    @Counted(value = "defaultExceptionHandler", extraTags = {"counter", "apiError"})
    public void defaultExceptionHandler() {
    }
}
