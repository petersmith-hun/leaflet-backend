package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntrySearchResultDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.InvalidTransitionException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.EntryFacade;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntriesController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntriesControllerTest extends AbstractControllerBaseTest {

    private static final List<EntryVO> ENTRY_VO_LIST = Collections.singletonList(ENTRY_VO);
    private static final long CATEGORY_ID = 5L;
    private static final long TAG_ID = 6L;
    private static final String ENTRY_LINK = "entry-link";
    private static final long ENTRY_ID = 1L;
    private static final EntryInitialStatus PUBLICATION_STATUS = EntryInitialStatus.PUBLIC;
    private static final String LOCATION_HEADER = "/entries/" + ENTRY_ID;
    private static final String CONTENT = "content";

    @Mock
    private EntryFacade entryFacade;

    @InjectMocks
    private EntriesController controller;

    @BeforeEach
    public void setup() {
        super.setup();
        given(conversionService.convert(ENTRY_VO_LIST, EntryListDataModel.class)).willReturn(ENTRY_LIST_DATA_MODEL);
        given(conversionService.convert(ENTRY_VO, ExtendedEntryDataModel.class)).willReturn(EXTENDED_ENTRY_DATA_MODEL);
        given(conversionService.convert(ENTRY_VO, EditEntryDataModel.class)).willReturn(EDIT_ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldGetAllEntries() {

        // given
        given(entryFacade.getAll()).willReturn(ENTRY_VO_LIST);

        // when
        ResponseEntity<EntryListDataModel> result = controller.getAllEntries();

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfPublicEntries() {

        // given
        given(entryFacade.getPageOfPublicEntries(PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.<EntryVO>getBuilder()
                .withEntitiesOnPage(ENTRY_VO_LIST)
                .build());

        // when
        ResponseEntity<EntryListDataModel> result = controller.getPageOfPublicEntries(PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfEntries() {

        // given
        given(entryFacade.getEntityPage(PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.<EntryVO>getBuilder()
                .withEntitiesOnPage(ENTRY_VO_LIST)
                .build());

        // when
        ResponseEntity<EntryListDataModel> result = controller.getPageOfEntries(PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfPublicEntriesByCategory() {

        // given
        given(entryFacade.getPageOfPublicEntriesUnderCategory(CATEGORY_ID, PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.<EntryVO>getBuilder()
                .withEntitiesOnPage(ENTRY_VO_LIST)
                .build());

        // when
        ResponseEntity<EntryListDataModel> result = controller.getPageOfPublicEntriesByCategory(CATEGORY_ID, PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfPublicEntriesByTag() {

        // given
        given(entryFacade.getPageOfPublicEntriesUnderTag(TAG_ID, PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.<EntryVO>getBuilder()
                .withEntitiesOnPage(ENTRY_VO_LIST)
                .build());

        // when
        ResponseEntity<EntryListDataModel> result = controller.getPageOfPublicEntriesByTag(TAG_ID, PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfPublicEntriesByContent() {

        // given
        given(entryFacade.getPageOfPublicEntriesByContent(CONTENT, PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.<EntryVO>getBuilder()
                .withEntitiesOnPage(ENTRY_VO_LIST)
                .build());

        // when
        ResponseEntity<EntryListDataModel> result = controller.getPageOfPublicEntriesByContent(CONTENT, PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetEntryByLink() throws EntityNotFoundException, ResourceNotFoundException {

        // given
        given(entryFacade.findByLink(ENTRY_LINK)).willReturn(ENTRY_VO);

        // when
        ResponseEntity<ExtendedEntryDataModel> result = controller.getEntryByLink(ENTRY_LINK);

        // then
        assertResponse(result, HttpStatus.OK, EXTENDED_ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldGetEntryByLinkWithEntityNotFoundException() throws EntityNotFoundException {

        // given
        doThrow(EntityNotFoundException.class).when(entryFacade).findByLink(ENTRY_LINK);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getEntryByLink(ENTRY_LINK));

        // then
        // exception expected
    }

    @Test
    public void shouldGetEntryById() throws ServiceException, ResourceNotFoundException {

        // given
        given(entryFacade.getOne(ENTRY_ID)).willReturn(ENTRY_VO);

        // when
        ResponseEntity<EditEntryDataModel> result = controller.getEntryByID(ENTRY_ID);

        // then
        assertResponse(result, HttpStatus.OK, EDIT_ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldGetEntryByLinkWithServiceException() throws ServiceException {

        // given
        doThrow(EntityNotFoundException.class).when(entryFacade).getOne(ENTRY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getEntryByID(ENTRY_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldSearchEntries() {

        // given
        given(conversionService.convert(ENTRY_SEARCH_PARAMETERS, EntrySearchParametersVO.class)).willReturn(ENTRY_SEARCH_PARAMETERS_VO);
        given(entryFacade.searchEntries(ENTRY_SEARCH_PARAMETERS_VO)).willReturn(EntityPageVO.<EntryVO>getBuilder().withEntitiesOnPage(ENTRY_VO_LIST).build());
        given(conversionService.convert(ENTRY_VO_LIST, EntrySearchResultDataModel.class)).willReturn(ENTRY_SEARCH_RESULT_DATA_MODEL);

        // when
        ResponseEntity<EntrySearchResultDataModel> result = controller.searchEntries(ENTRY_SEARCH_PARAMETERS);

        // then
        assertResponse(result, HttpStatus.OK, ENTRY_SEARCH_RESULT_DATA_MODEL);
    }

    @Test
    public void shouldCreateEntry() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        given(entryFacade.createOne(ENTRY_VO)).willReturn(ENTRY_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createEntry(ENTRY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_ENTRY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateEntryWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createEntry(ENTRY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldCreateEntryWithConstraintViolation() throws ServiceException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        doThrow(ConstraintViolationException.class).when(entryFacade).createOne(ENTRY_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createEntry(ENTRY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateEntryWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        doThrow(ServiceException.class).when(entryFacade).createOne(ENTRY_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createEntry(ENTRY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateEntry() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        given(entryFacade.updateOne(ENTRY_ID, ENTRY_VO)).willReturn(ENTRY_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateEntry(ENTRY_ID, ENTRY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_ENTRY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateEntryWithValidationError() throws RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateEntry(ENTRY_ID, ENTRY_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldUpdateEntryWithConstraintViolation() throws ServiceException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        doThrow(ConstraintViolationException.class).when(entryFacade).updateOne(ENTRY_ID, ENTRY_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.updateEntry(ENTRY_ID, ENTRY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateEntryWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(ENTRY_CREATE_REQUEST_MODEL, EntryVO.class)).willReturn(ENTRY_VO);
        doThrow(ServiceException.class).when(entryFacade).updateOne(ENTRY_ID, ENTRY_VO);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.updateEntry(ENTRY_ID, ENTRY_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldChangeStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(entryFacade.changeStatus(ENTRY_ID)).willReturn(ENTRY_VO);

        // when
        ResponseEntity<EditEntryDataModel> result = controller.changeStatus(ENTRY_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_ENTRY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldChangeStatusWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(entryFacade).changeStatus(ENTRY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.changeStatus(ENTRY_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldChangePublicationStatus() throws ServiceException, RequestCouldNotBeFulfilledException, ResourceNotFoundException {

        // given
        given(entryFacade.changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS.name())).willReturn(ENTRY_VO);
        given(conversionService.convert(ENTRY_VO, EditEntryDataModel.class)).willReturn(EDIT_ENTRY_DATA_MODEL);

        // when
        ResponseEntity<EditEntryDataModel> result = controller.changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS);

        // then
        assertResponse(result, HttpStatus.CREATED, EDIT_ENTRY_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldChangePublicationStatusHandleInvalidTransitionException() throws ServiceException {

        // given
        doThrow(InvalidTransitionException.class).when(entryFacade).changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS.name());

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS));

        // then
        // exception expected
    }

    @Test
    public void shouldChangePublicationStatusHandleServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(entryFacade).changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS.name());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.changePublicationStatus(ENTRY_ID, PUBLICATION_STATUS));

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteEntry() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteEntry(ENTRY_ID);

        // then
        verify(entryFacade).deletePermanently(ENTRY_ID);
    }

    @Test
    public void shouldDeleteEntryWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(entryFacade).deletePermanently(ENTRY_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.deleteEntry(ENTRY_ID));

        // then
        // exception expected
    }
}