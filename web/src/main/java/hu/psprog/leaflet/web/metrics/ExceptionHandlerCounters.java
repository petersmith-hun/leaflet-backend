package hu.psprog.leaflet.web.metrics;

import com.codahale.metrics.annotation.Counted;
import org.springframework.stereotype.Component;

/**
 * Counter metrics for exception handler.
 *
 * @author Peter Smith
 */
@Component
public class ExceptionHandlerCounters {

    @Counted(monotonic = true)
    public void resourceNotFound() {
    }

    @Counted(monotonic = true)
    public void authenticationFailure() {
    }

    @Counted(monotonic = true)
    public void authorizationFailure() {
    }

    @Counted(monotonic = true)
    public void requestCouldNotBeFulfilled() {
    }

    @Counted(monotonic = true)
    public void defaultExceptionHandler() {
    }
}
