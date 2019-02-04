package hu.psprog.leaflet.web.rest.controller;

import com.codahale.metrics.annotation.Timed;
import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.ContactService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for contact request processing.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(path = BaseController.BASE_PATH_CONTACT)
public class ContactRequestController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactRequestController.class);

    private ContactService contactService;

    @Autowired
    public ContactRequestController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * POST /contact
     * Processes a contact request.
     *
     * @param contactRequestModel {@link ContactRequestModel} containing identification data and the message of a user
     * @param bindingResult validation results
     * @return empty response or validation results
     * @throws RequestCouldNotBeFulfilledException thrown when an unexpected exception occurs during processing the request
     */
    @PostMapping
    @Timed
    public ResponseEntity<BaseBodyDataModel> processContactRequest(@RequestBody @Valid ContactRequestModel contactRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                ContactRequestVO contactRequestVO = conversionService.convert(contactRequestModel, ContactRequestVO.class);
                contactService.processContactRequest(contactRequestVO);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .build();
            } catch (Exception e) {
                LOGGER.error("Failed to process contact request [{}].", contactRequestModel, e);
                throw new RequestCouldNotBeFulfilledException("Failed to process contact request");
            }
        }
    }
}
