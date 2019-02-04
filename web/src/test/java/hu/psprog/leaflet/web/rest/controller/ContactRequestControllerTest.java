package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.ContactService;
import hu.psprog.leaflet.service.vo.ContactRequestVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link ContactRequestController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactRequestControllerTest extends AbstractControllerBaseTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactRequestController contactRequestController;

    @Test
    public void shouldProcessContactRequest() throws RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(CONTACT_REQUEST_MODEL, ContactRequestVO.class)).willReturn(CONTACT_REQUEST_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = contactRequestController.processContactRequest(CONTACT_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(contactService).processContactRequest(CONTACT_REQUEST_VO);
    }

    @Test
    public void shouldProcessContactRequestWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = contactRequestController.processContactRequest(CONTACT_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
        verifyZeroInteractions(contactService, contactService);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldProcessContactRequestWithException() throws RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(CONTACT_REQUEST_MODEL, ContactRequestVO.class)).willReturn(CONTACT_REQUEST_VO);
        doThrow(RuntimeException.class).when(contactService).processContactRequest(CONTACT_REQUEST_VO);

        // when
        contactRequestController.processContactRequest(CONTACT_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }
}
