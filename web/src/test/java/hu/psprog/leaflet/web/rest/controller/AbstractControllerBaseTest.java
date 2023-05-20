package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.web.metrics.ExceptionHandlerCounters;
import hu.psprog.leaflet.web.test.ConversionTestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.net.URI;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Base controller test.
 *
 * @author Peter Smith
 */
abstract class AbstractControllerBaseTest extends ConversionTestObjects {

    static final int PAGE = 1;
    static final int LIMIT = 10;
    static final String DIRECTION = "ASC";
    static final String ORDER_BY = "CREATED";
    
    @Mock(strictness = Mock.Strictness.LENIENT)
    BindingResult bindingResult;

    @Mock(strictness = Mock.Strictness.LENIENT)
    ConversionService conversionService;

    @Mock
    ExceptionHandlerCounters exceptionHandlerCounters;

    @BeforeEach
    public void setup() {
        given(bindingResult.getAllErrors()).willReturn(OBJECT_ERROR_LIST);
        given(conversionService.convert(OBJECT_ERROR_LIST, ValidationErrorMessageListDataModel.class))
                .willReturn(VALIDATION_ERROR_MESSAGE_LIST_DATA_MODEL);
    }

    void givenValidationError() {
        given(bindingResult.hasErrors()).willReturn(true);
    }

    void assertResponse(ResponseEntity<?> responseEntity, HttpStatus expectedStatus, BaseBodyDataModel expectedBody) {
        assertResponse(responseEntity, expectedStatus, expectedBody, null);
    }

    void assertResponse(ResponseEntity<?> responseEntity, HttpStatus expectedStatus, BaseBodyDataModel expectedBody, String locationHeader) {
        assertThat(responseEntity.getStatusCode(), equalTo(expectedStatus));
        assertThat(responseEntity.getBody(), equalTo(expectedBody));
        if (Objects.nonNull(locationHeader)) {
            assertThat(responseEntity.getHeaders().getLocation(), equalTo(URI.create(locationHeader)));
        }
    }

    void assertValidationError(ResponseEntity<? extends BaseBodyDataModel> responseEntity) {
        assertResponse(responseEntity, HttpStatus.BAD_REQUEST, VALIDATION_ERROR_MESSAGE_LIST_DATA_MODEL);
    }
}
