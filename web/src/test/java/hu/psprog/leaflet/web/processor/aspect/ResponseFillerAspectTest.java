package hu.psprog.leaflet.web.processor.aspect;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.common.SEODataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.service.facade.EntryFacade;
import hu.psprog.leaflet.web.rest.controller.EntriesController;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ResponseFillerAspect}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ResponseFillerAspectTest {

    private static final EntryDataModel ENTRY_DATA_MODEL = EntryDataModel.getBuilder()
            .withLink("entry-link")
            .build();
    private static final ExtendedEntryDataModel EXTENDED_ENTRY_DATA_MODEL = ExtendedEntryDataModel.getExtendedBuilder()
            .withLink("entry-link")
            .build();
    private static final EntryListDataModel ENTRY_LIST_DATA_MODEL = EntryListDataModel.getBuilder()
            .withItem(ENTRY_DATA_MODEL)
            .build();
    private static final ErrorMessageDataModel ERROR_MESSAGE_DATA_MODEL = ErrorMessageDataModel.getBuilder()
            .withMessage("Forbidden")
            .build();
    private static final SEODataModel TEST_WRAPPING = SEODataModel.getBuilder()
            .withPageTitle("Test wrapping")
            .build();

    @Mock
    private EntryFacade entryFacade;

    @Mock
    private ConversionService conversionService;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    private ResponseFillerAspect responseFillerAspect;
    private MockedResponseFiller responseFiller;

    @Test
    public void shouldWrapAndFillResponseWithStandardBody() throws Throwable {

        // given
        prepareAspect(true);
        given(proceedingJoinPoint.proceed()).willReturn(prepareStandardResponse());

        // when
        Object result = responseFillerAspect.aspectToWrapAnswer(proceedingJoinPoint);

        // then
        assertWrappedStandardResponse(result, ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldWrapAndFillResponseWithErrorBody() throws Throwable {

        // given
        prepareAspect(true);
        given(proceedingJoinPoint.proceed()).willReturn(prepareErrorResponse());

        // when
        Object result = responseFillerAspect.aspectToWrapAnswer(proceedingJoinPoint);

        // then
        assertThat(result, notNullValue());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
        assertThat(responseEntity.getBody(), equalTo(WrapperBodyDataModel.getBuilder()
                .withError(ERROR_MESSAGE_DATA_MODEL)
                .withSeo(TEST_WRAPPING)
                .build()));

    }

    @Test
    public void shouldOnlyWrapResponse() throws Throwable {

        // given
        prepareAspect(false);
        given(proceedingJoinPoint.proceed()).willReturn(prepareStandardResponse());

        // when
        Object result = responseFillerAspect.aspectToWrapAnswer(proceedingJoinPoint);

        // then
        assertThat(result, notNullValue());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody(), equalTo(WrapperBodyDataModel.getBuilder()
                .withBody(ENTRY_DATA_MODEL)
                .build()));
    }

    @Test
    public void shouldNotWrapUnsupportedResponse() throws Throwable {

        // given
        prepareAspect(false);
        ResponseEntity<String> response = prepareUnsupportedResponse();
        given(proceedingJoinPoint.proceed()).willReturn(response);

        // when
        Object result = responseFillerAspect.aspectToWrapAnswer(proceedingJoinPoint);

        // then
        assertThat(result, equalTo(response));
    }

    @Test
    public void shouldAspectBeActivated() throws Throwable {

        // given
        prepareAspect(true);
        given(conversionService.convert(any(), any())).willReturn(EXTENDED_ENTRY_DATA_MODEL);

        // when
        ResponseEntity<?> result = preparePointcut().getEntryByLink("link");

        // then
        assertWrappedStandardResponse(result, EXTENDED_ENTRY_DATA_MODEL);
    }

    @Test
    public void shouldAspectSkipProcessing() {

        // given
        prepareAspect(true);
        given(conversionService.convert(any(), any())).willReturn(ENTRY_LIST_DATA_MODEL);

        // when
        ResponseEntity<?> result = preparePointcut().getAllEntries();

        // then
        verifyNoInteractions(proceedingJoinPoint);
        assertThat(result, equalTo(ResponseEntity.ok(ENTRY_LIST_DATA_MODEL)));
    }

    private void assertWrappedStandardResponse(Object result, EntryDataModel entryDataModel) {
        assertThat(result, notNullValue());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody(), equalTo(WrapperBodyDataModel.getBuilder()
                .withBody(entryDataModel)
                .withSeo(TEST_WRAPPING)
                .build()));
    }

    private EntriesController preparePointcut() {

        EntriesController controller = prepareEntriesControllerMock();
        AspectJProxyFactory factory = new AspectJProxyFactory(controller);
        factory.addAspect(responseFillerAspect);

        return factory.getProxy();
    }

    private EntriesController prepareEntriesControllerMock() {

        EntriesController controller = new EntriesController(entryFacade);
        Field conversionServiceField = ReflectionUtils.findField(EntriesController.class, "conversionService");
        conversionServiceField.setAccessible(true);
        ReflectionUtils.setField(conversionServiceField, controller, conversionService);

        return controller;
    }

    private void prepareAspect(boolean shouldFill) {
        responseFiller = MockedResponseFiller.create(shouldFill);
        responseFillerAspect = new ResponseFillerAspect(Collections.singletonList(responseFiller));
    }

    private ResponseEntity<BaseBodyDataModel> prepareStandardResponse() {
        return ResponseEntity
                .ok(ENTRY_DATA_MODEL);
    }

    private ResponseEntity<BaseBodyDataModel> prepareErrorResponse() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ERROR_MESSAGE_DATA_MODEL);
    }

    private ResponseEntity<String> prepareUnsupportedResponse() {
        return ResponseEntity
                .ok("unsupported response body type");
    }

    private static class MockedResponseFiller implements ResponseFiller {

        private boolean shouldFill;

        private MockedResponseFiller(boolean shouldFill) {
            this.shouldFill = shouldFill;
        }

        static MockedResponseFiller create(boolean shouldFill) {
            return new MockedResponseFiller(shouldFill);
        }

        @Override
        public void fill(WrapperBodyDataModel.WrapperBodyDataModelBuilder wrapperBodyDataModelBuilder) {
            wrapperBodyDataModelBuilder.withSeo(TEST_WRAPPING);
        }

        @Override
        public boolean shouldFill() {
            return shouldFill;
        }
    }
}