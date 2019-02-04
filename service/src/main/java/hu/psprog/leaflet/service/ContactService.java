package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.vo.ContactRequestVO;

/**
 * Contact service operations interface.
 *
 * @author Peter Smith
 */
public interface ContactService {

    /**
     * Processes the given {@link ContactRequestVO} object.
     *
     * @param contactRequestVO {@link ContactRequestVO} object to be processed
     */
    void processContactRequest(ContactRequestVO contactRequestVO);
}
