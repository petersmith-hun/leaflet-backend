package hu.psprog.leaflet.service.facade;

import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;

/**
 * Facade for {@link AttachmentService}.
 *
 * @author Peter Smith
 */
public interface AttachmentFacade {

    /**
     * Attaches an existing uploaded file to the given entry.
     *
     * @param attachmentRequestVO attachment request object
     */
    void attachFileToEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException;

    /**
     * Detaches an attachment from an entry.
     *
     * @param attachmentRequestVO attachment request object
     */
    void detachFileFromEntry(AttachmentRequestVO attachmentRequestVO) throws ServiceException;
}
