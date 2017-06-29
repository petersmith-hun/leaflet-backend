package hu.psprog.leaflet.web.config.listener;

import io.reactivex.schedulers.Schedulers;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Application shutdown event listener.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

        // shutdown Rx worker threads
        Schedulers.shutdown();
    }
}
