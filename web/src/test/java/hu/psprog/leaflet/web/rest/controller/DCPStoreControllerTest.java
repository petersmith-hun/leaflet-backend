package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.dcp.DCPRequestModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPDataModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPListDataModel;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DCPStoreController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DCPStoreControllerTest extends AbstractControllerBaseTest {

    private static final String KEY_1 = "key-1";
    private static final String KEY_2 = "key-2";
    private static final String VALUE_1 = "value-1";
    private static final String VALUE_2 = "value-2";

    @Mock
    private DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    @InjectMocks
    private DCPStoreController controller;

    @Test
    public void shouldGetAllEntries() {

        // given
        given(dynamicConfigurationPropertyService.getAll()).willReturn(prepareAllDCPEntries());

        // when
        ResponseEntity<DCPListDataModel> result = controller.getAll();

        // then
        assertResponse(result, HttpStatus.OK, prepareDcpListDataModel());
    }

    @Test
    public void shouldCreate() throws RequestCouldNotBeFulfilledException, ServiceException {

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.create(prepareDcpRequestModel(), bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(dynamicConfigurationPropertyService).add(KEY_1, VALUE_1);
    }

    @Test
    public void shouldCreateWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(dynamicConfigurationPropertyService).add(KEY_1, VALUE_1);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.create(prepareDcpRequestModel(), bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.create(prepareDcpRequestModel(), bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldUpdate() throws RequestCouldNotBeFulfilledException, ServiceException {

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.update(prepareDcpRequestModel(), bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(dynamicConfigurationPropertyService).update(KEY_1, VALUE_1);
    }

    @Test
    public void shouldUpdateWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(dynamicConfigurationPropertyService).update(KEY_1, VALUE_1);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.update(prepareDcpRequestModel(), bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.update(prepareDcpRequestModel(), bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldRemove() throws RequestCouldNotBeFulfilledException, ServiceException {

        // when
        ResponseEntity<Void> result = controller.remove(KEY_1);

        // then
        assertResponse(result, HttpStatus.NO_CONTENT, null);
        verify(dynamicConfigurationPropertyService).delete(KEY_1);
    }

    @Test
    public void shouldRemoveWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(dynamicConfigurationPropertyService).delete(KEY_1);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.remove(KEY_1));

        // then
        // exception expected
    }

    private Map<String, String> prepareAllDCPEntries() {

        Map<String, String> dcp = new HashMap<>();
        dcp.put(KEY_1, VALUE_1);
        dcp.put(KEY_2, VALUE_2);

        return dcp;
    }

    private DCPListDataModel prepareDcpListDataModel() {
        return DCPListDataModel.getBuilder()
                .withItem(prepareDcpDataModel(KEY_1, VALUE_1))
                .withItem(prepareDcpDataModel(KEY_2, VALUE_2))
                .build();
    }

    private DCPDataModel prepareDcpDataModel(String key, String value) {
        return DCPDataModel.getBuilder()
                .withKey(key)
                .withValue(value)
                .build();
    }

    private DCPRequestModel prepareDcpRequestModel() {

        DCPRequestModel dcpRequestModel = new DCPRequestModel();
        dcpRequestModel.setKey(KEY_1);
        dcpRequestModel.setValue(VALUE_1);

        return dcpRequestModel;
    }
}