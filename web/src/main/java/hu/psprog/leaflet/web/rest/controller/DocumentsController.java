package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.DocumentFacade;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.annotation.FillResponse;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.net.URI;
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

    private final DocumentFacade documentFacade;

    @Autowired
    public DocumentsController(ConversionService conversionService, ExceptionHandlerCounters exceptionHandlerCounters,
                               DocumentFacade documentFacade) {
        super(conversionService, exceptionHandlerCounters);
        this.documentFacade = documentFacade;
    }

    /**
     * GET /documents
     * Returns basic information of all existing document.
     *
     * @return list of existing documents
     */
    @RequestMapping(method = RequestMethod.GET)
    @Timed(value = "getAllDocuments", extraTags = {"controller", "documents"})
    public ResponseEntity<DocumentListDataModel> getAllDocuments() {

        List<DocumentVO> documentVOList = documentFacade.getAll();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(documentVOList, DocumentListDataModel.class));
    }

    /**
     * GET /documents/public
     * Returns basic information of existing public documents.
     *
     * @return list of public documents
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PUBLIC)
    @Timed(value = "getPublicDocuments", extraTags = {"controller", "documents"})
    public ResponseEntity<DocumentListDataModel> getPublicDocuments() {

        List<DocumentVO> documentVOList = documentFacade.getPublicDocuments();

        return ResponseEntity
                .ok()
                .body(conversionService.convert(documentVOList, DocumentListDataModel.class));
    }

    /**
     * GET /documents/{id}
     * Returns detailed information of document identified by given ID (for admin usage).
     *
     * @param id ID of an existing document
     * @return identified document
     * @throws ResourceNotFoundException if no document associated with given ID exists
     */
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, path = PATH_PART_ID)
    @Timed(value = "getDocumentByID", extraTags = {"controller", "documents"})
    public ResponseEntity<EditDocumentDataModel> getDocumentByID(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            DocumentVO documentVO = documentFacade.getOne(id);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(documentVO, EditDocumentDataModel.class));
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
    @FillResponse
    @RequestMapping(method = RequestMethod.GET, path = PATH_DOCUMENT_BY_LINK)
    @Timed(value = "getDocumentByLink", extraTags = {"controller", "documents"})
    public ResponseEntity<DocumentDataModel> getDocumentByLink(@PathVariable(PATH_VARIABLE_LINK) String link) throws ResourceNotFoundException {

        try {
            DocumentVO documentVO = documentFacade.getByLink(link);

            return ResponseEntity
                    .ok()
                    .body(conversionService.convert(documentVO, DocumentDataModel.class));
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
    public ResponseEntity<BaseBodyDataModel> createDocument(@RequestBody @Valid DocumentCreateRequestModel documentCreateRequestModel,
                                                            BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                DocumentVO createdDocument = documentFacade.createOne(conversionService.convert(documentCreateRequestModel, DocumentVO.class));

                return ResponseEntity
                        .created(buildLocation(createdDocument.getId()))
                        .body(conversionService.convert(createdDocument, EditDocumentDataModel.class));
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
    public ResponseEntity<BaseBodyDataModel> updateDocument(@PathVariable(PATH_VARIABLE_ID) Long id,
                                       @RequestBody @Valid DocumentUpdateRequestModel documentUpdateRequestModel,
                                       BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return validationFailureResponse(bindingResult);
        } else {
            try {
                DocumentVO updatedDocument = documentFacade.updateOne(id, conversionService.convert(documentUpdateRequestModel, DocumentVO.class));

                return ResponseEntity
                        .created(buildLocation(id))
                        .body(conversionService.convert(updatedDocument, EditDocumentDataModel.class));
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
     * PUT /documents/{id}/status
     * Changes status of an existing document.
     *
     * @param id ID of the document to update status of
     * @return updated document data
     * @throws ResourceNotFoundException if no document associated with given id exists
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_CHANGE_STATUS)
    public ResponseEntity<EditDocumentDataModel> changeStatus(@PathVariable(PATH_VARIABLE_ID) Long id) throws ResourceNotFoundException {

        try {
            DocumentVO updatedDocument = documentFacade.changeStatus(id);

            return ResponseEntity
                    .created(buildLocation(id))
                    .body(conversionService.convert(updatedDocument, EditDocumentDataModel.class));

        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
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
            documentFacade.deletePermanently(id);
        } catch (ServiceException e) {
            LOGGER.error(REQUESTED_DOCUMENT_NOT_FOUND, e);
            throw new ResourceNotFoundException(THE_DOCUMENT_YOU_ARE_LOOKING_FOR_IS_NOT_EXISTING);
        }
    }

    private URI buildLocation(Long id) {
        return URI.create(BASE_PATH_DOCUMENTS + "/" + id);
    }
}
