package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.DocumentFacade;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentsController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentsControllerTest extends AbstractControllerBaseTest {

    private static final List<DocumentVO> DOCUMENT_VO_LIST = Collections.singletonList(DOCUMENT_VO_WITH_OWNER);
    private static final long DOCUMENT_ID = 1L;
    private static final String LOCATION_HEADER = "/documents/" + DOCUMENT_ID;
    private static final String DOCUMENT_LINK = "link";

    @Mock
    private DocumentFacade documentFacade;

    @InjectMocks
    private DocumentsController controller;

    @Before
    public void setup() {
        super.setup();
        given(conversionService.convert(DOCUMENT_VO_LIST, DocumentListDataModel.class)).willReturn(DOCUMENT_LIST_DATA_MODEL);
        given(conversionService.convert(DOCUMENT_VO_WITH_OWNER, DocumentDataModel.class)).willReturn(DOCUMENT_DATA_MODEL);
        given(conversionService.convert(DOCUMENT_VO_WITH_OWNER, EditDocumentDataModel.class)).willReturn(EDIT_DOCUMENT_DATA_MODEL);
    }

    @Test
    public void shouldGetAllDocuments() {

        // given
        given(documentFacade.getAll()).willReturn(DOCUMENT_VO_LIST);

        // when
        ResponseEntity<DocumentListDataModel> result = controller.getAllDocuments();

        // then
        assertResponse(result, HttpStatus.OK, DOCUMENT_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPublicDocuments() {

        // given
        given(documentFacade.getPublicDocuments()).willReturn(DOCUMENT_VO_LIST);

        // when
        ResponseEntity<DocumentListDataModel> result = controller.getPublicDocuments();

        // then
        assertResponse(result, HttpStatus.OK, DOCUMENT_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetDocumentById() throws ServiceException, ResourceNotFoundException {

        // given
        given(documentFacade.getOne(DOCUMENT_ID)).willReturn(DOCUMENT_VO_WITH_OWNER);

        // when
        ResponseEntity<EditDocumentDataModel> result = controller.getDocumentByID(DOCUMENT_ID);

        // then
        assertResponse(result, HttpStatus.OK, EDIT_DOCUMENT_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetDocumentByIdWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(documentFacade).getOne(DOCUMENT_ID);

        // when
        controller.getDocumentByID(DOCUMENT_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldGetDocumentByLink() throws ServiceException, ResourceNotFoundException {

        // given
        given(documentFacade.getByLink(DOCUMENT_LINK)).willReturn(DOCUMENT_VO_WITH_OWNER);

        // when
        ResponseEntity<DocumentDataModel> result = controller.getDocumentByLink(DOCUMENT_LINK);

        // then
        assertResponse(result, HttpStatus.OK, DOCUMENT_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetDocumentByLinkWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(documentFacade).getByLink(DOCUMENT_LINK);

        // when
        controller.getDocumentByLink(DOCUMENT_LINK);

        // then
        // exception expected
    }

    @Test
    public void shouldCreateDocument() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(DOCUMENT_CREATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        given(documentFacade.createOne(DOCUMENT_VO_WITH_OWNER)).willReturn(DOCUMENT_VO_WITH_OWNER);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createDocument(DOCUMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_DOCUMENT_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateDocumentWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createDocument(DOCUMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateDocumentWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(DOCUMENT_CREATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        doThrow(ConstraintViolationException.class).when(documentFacade).createOne(DOCUMENT_VO_WITH_OWNER);

        // when
        controller.createDocument(DOCUMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // expected exception
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateDocumentWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(DOCUMENT_CREATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        doThrow(ServiceException.class).when(documentFacade).createOne(DOCUMENT_VO_WITH_OWNER);

        // when
        controller.createDocument(DOCUMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // expected exception
    }

    @Test
    public void shouldUpdateDocument() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(conversionService.convert(DOCUMENT_UPDATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        given(documentFacade.updateOne(DOCUMENT_ID, DOCUMENT_VO_WITH_OWNER)).willReturn(DOCUMENT_VO_WITH_OWNER);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateDocument(DOCUMENT_ID, DOCUMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_DOCUMENT_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateDocumentWithValidationError() throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateDocument(DOCUMENT_ID, DOCUMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldUpdateDocumentWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(conversionService.convert(DOCUMENT_UPDATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        doThrow(ConstraintViolationException.class).when(documentFacade).updateOne(DOCUMENT_ID, DOCUMENT_VO_WITH_OWNER);

        // when
        controller.updateDocument(DOCUMENT_ID, DOCUMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // expected exception
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateDocumentWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(conversionService.convert(DOCUMENT_UPDATE_REQUEST_MODEL, DocumentVO.class)).willReturn(DOCUMENT_VO_WITH_OWNER);
        doThrow(ServiceException.class).when(documentFacade).updateOne(DOCUMENT_ID, DOCUMENT_VO_WITH_OWNER);

        // when
        controller.updateDocument(DOCUMENT_ID, DOCUMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // expected exception
    }

    @Test
    public void shouldChangeStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(documentFacade.changeStatus(DOCUMENT_ID)).willReturn(DOCUMENT_VO_WITH_OWNER);

        // when
        ResponseEntity<EditDocumentDataModel> result = controller.changeStatus(DOCUMENT_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_DOCUMENT_DATA_MODEL, LOCATION_HEADER);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldChangeStatusWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(documentFacade).changeStatus(DOCUMENT_ID);

        // when
        controller.changeStatus(DOCUMENT_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteDocument() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteDocument(DOCUMENT_ID);

        // then
        verify(documentFacade).deletePermanently(DOCUMENT_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteDocumentWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        doThrow(ServiceException.class).when(documentFacade).deletePermanently(DOCUMENT_ID);

        // when
        controller.deleteDocument(DOCUMENT_ID);

        // then
        // exception expected
    }
}