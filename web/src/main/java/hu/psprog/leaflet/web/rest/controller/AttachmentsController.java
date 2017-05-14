package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.attachment.AttachmentRequestModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.AttachmentFacade;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * REST controller for attachment operations.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_ATTACHMENTS)
public class AttachmentsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentsController.class);

    private AttachmentFacade attachmentFacade;

    @Autowired
    public AttachmentsController(AttachmentFacade attachmentFacade) {
        this.attachmentFacade = attachmentFacade;
    }

    /**
     * POST /attachments
     * Attaches an existing file to an existing entry.
     *
     * @param attachmentRequestModel attachment request
     * @param bindingResult validation result
     * @return validation errors or {@code null} if request is fulfilled
     * @throws ResourceNotFoundException if one of or both entities are not found
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView attach(@RequestBody @Valid AttachmentRequestModel attachmentRequestModel, BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                attachmentFacade.attachFileToEntry(conversionService.convert(attachmentRequestModel, AttachmentRequestVO.class));
            } catch (ServiceException e) {
                LOGGER.error("Failed to attach file to entry", e);
                throw new ResourceNotFoundException("Cannot attach non-existing entities to each other.", e);
            }
            return null;
        }
    }

    /**
     * DELETE /attachments
     * Detaches an existing file from an existing entry.
     *
     * @param attachmentRequestModel attachment request
     * @param bindingResult validation result
     * @return validation errors or {@code null} if request is fulfilled
     * @throws ResourceNotFoundException if one of or both entities are not found
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView detach(@RequestBody @Valid AttachmentRequestModel attachmentRequestModel, BindingResult bindingResult)
            throws ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                attachmentFacade.detachFileFromEntry(conversionService.convert(attachmentRequestModel, AttachmentRequestVO.class));
            } catch (ServiceException e) {
                LOGGER.error("Failed to detach file from entry", e);
                throw new ResourceNotFoundException("Cannot detach non-existing entities to each other.", e);
            }
            return null;
        }
    }
}
