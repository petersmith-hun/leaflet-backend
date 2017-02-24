package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.rest.conversion.ValidationErrorMessagesConverter;
import hu.psprog.leaflet.web.rest.conversion.document.DocumentUpdateRequestModelToDocumentVOConverter;
import hu.psprog.leaflet.web.rest.conversion.document.DocumentVOToDocumentDataModelEntityConverter;
import hu.psprog.leaflet.web.rest.conversion.document.DocumentVOToEditDocumentDataModelEntityConverter;
import hu.psprog.leaflet.web.rest.conversion.document.DocumentVOToEditDocumentDataModelListConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for document related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(BaseController.BASE_PATH_DOCUMENTS)
public class DocumentsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsController.class);

    private static final String PATH_DOCUMENT_BY_LINK = "/link" + PATH_PART_LINK;

    private static final String REQUESTED_DOCUMENT_NOT_FOUND = "Requested document not found";
    private static final String THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING = "The document you are looking for is not existing.";
    private static final String A_DOCUMENT_WITH_THE_SAME_LINK_ALREADY_EXISTS = "A document with the same link already exists.";
    private static final String DOCUMENT_COULD_NOT_BE_CREATED_SEE_DETAILS = "Document could not be created, please try again later.";
    private static final String DOCUMENT_COULD_NOT_BE_CREATED = "Document could not be created. See details: ";

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentUpdateRequestModelToDocumentVOConverter documentUpdateRequestModelToDocumentVOConverter;

    @Autowired
    private DocumentVOToDocumentDataModelEntityConverter documentVOToDocumentDataModelEntityConverter;

    @Autowired
    private DocumentVOToEditDocumentDataModelEntityConverter documentVOToEditDocumentDataModelEntityConverter;

    @Autowired
    private DocumentVOToEditDocumentDataModelListConverter documentVOToEditDocumentDataModelListConverter;

    @Autowired
    private ValidationErrorMessagesConverter validationErrorMessagesConverter;

    /**
     * GET /documents
     * Returns basic information of all existing document.
     *
     * @return list of existing documents
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAllDocuments() {

        List<DocumentVO> documentVOList = documentService.getAll();

        return wrap(documentVOToEditDocumentDataModelListConverter.convert(documentVOList));
    }

    /**
     * GET /documents/{id}
     * Returns detailed information of document identified by given ID (for admin usage).
     *
     * @param id ID of an existing document
     * @return identified document
     * @throws ResourceNotFoundException if no document associated with given ID exists
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    public ModelAndView getDocumentByID(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            DocumentVO documentVO = documentService.getOne(id);

            return wrap(documentVOToEditDocumentDataModelEntityConverter.convert(documentVO));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * GET /document/{link}
     * Returns details detailed information of document identified by given link (for visitors).
     *
     * @param link link to identify document
     * @return identified document
     * @throws ResourceNotFoundException if no document associated with given link exists
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_DOCUMENT_BY_LINK)
    public ModelAndView getDocumentByLink(@PathVariable(PATH_VARIABLE_LINK) String link) throws ResourceNotFoundException {

        try {
            DocumentVO documentVO = documentService.getByLink(link);

            return wrap(documentVOToEditDocumentDataModelEntityConverter.convert(documentVO));
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    /**
     * POST /documents
     * Creates a new document.
     *
     * @param documentCreateRequestModel document data
     * @param bindingResult validation results
     * @return created document data
     * @throws RequestCouldNotBeFulfilledException if a constraint violation or any other service error happens during processing the request
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView createDocument(@RequestBody @Valid DocumentCreateRequestModel documentCreateRequestModel,
                                       BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(validationErrorMessagesConverter.convert(bindingResult.getAllErrors()));
        } else {
            try {
                Long documentID = documentService.createOne(documentUpdateRequestModelToDocumentVOConverter.convert(documentCreateRequestModel));
                DocumentVO createdDocument = documentService.getOne(documentID);

                return wrap(documentVOToEditDocumentDataModelEntityConverter.convert(createdDocument));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(A_DOCUMENT_WITH_THE_SAME_LINK_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(DOCUMENT_COULD_NOT_BE_CREATED_SEE_DETAILS, e);
                throw new RequestCouldNotBeFulfilledException(DOCUMENT_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * PUT /documents/{id}
     * Updates an existing document.
     *
     * @param id ID of an existing document
     * @param documentUpdateRequestModel document data
     * @param bindingResult validation results
     * @return updated document data
     * @throws RequestCouldNotBeFulfilledException if a constraint violation or any other service error happens during processing the request
     * @throws ResourceNotFoundException if no document associated with given id exists
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_PART_ID)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView updateDocument(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid DocumentUpdateRequestModel documentUpdateRequestModel,
                                       BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return wrap(validationErrorMessagesConverter.convert(bindingResult.getAllErrors()));
        } else {
            try {
                documentService.updateOne(id, documentUpdateRequestModelToDocumentVOConverter.convert(documentUpdateRequestModel));
                DocumentVO updatedDocument = documentService.getOne(id);

                return wrap(documentVOToEditDocumentDataModelEntityConverter.convert(updatedDocument));
            } catch (ConstraintViolationException e) {
                LOGGER.error(CONSTRAINT_VIOLATION, e);
                throw new RequestCouldNotBeFulfilledException(A_DOCUMENT_WITH_THE_SAME_LINK_ALREADY_EXISTS);
            } catch (ServiceException e) {
                LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
                throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
            }
        }
    }

    /**
     * DELETE /documents/{id}
     * Deletes an existing document.
     *
     * @param id ID of an existing document
     * @throws ResourceNotFoundException if no document associated with given id exists
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PART_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            DocumentVO documentVO = documentService.getOne(id);
            documentService.deleteByEntity(documentVO);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }
}
