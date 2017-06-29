package hu.psprog.leaflet.service;

/**
 * System notification handler service.
 *
 * @author Peter Smith
 */
public interface NotificationService {

    /**
     * Sends notification after system startup.
     *
     * @param version version of currently running application
     */
    void startupFinished(String version);
}
