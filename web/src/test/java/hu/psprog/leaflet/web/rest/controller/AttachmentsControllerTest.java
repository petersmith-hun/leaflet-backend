package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.AttachmentFacade;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link AttachmentsController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AttachmentsControllerTest extends AbstractControllerBaseTest {

    @Mock
    private AttachmentFacade attachmentFacade;

    @InjectMocks
    private AttachmentsController controller;

    @Test
    public void shouldAttach() throws ResourceNotFoundException, ServiceException {

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.attach(ATTACHMENT_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.OK, null);
        verify(attachmentFacade).attachFileToEntry(any());
    }

    @Test
    public void shouldAttachWithServiceFailure() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(attachmentFacade).attachFileToEntry(any());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.attach(ATTACHMENT_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldAttachWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.attach(ATTACHMENT_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldDetach() throws ResourceNotFoundException, ServiceException {

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.detach(ATTACHMENT_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.OK, null);
        verify(attachmentFacade).detachFileFromEntry(any());
    }

    @Test
    public void shouldDetachWithServiceFailure() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(attachmentFacade).detachFileFromEntry(any());

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.detach(ATTACHMENT_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldDetachWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.detach(ATTACHMENT_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }
}