package hu.psprog.leaflet.web.processor.aspect;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.web.rest.filler.ResponseFiller;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Response filler aspect to handle filling common data in response.
 *
 * @author Peter Smith
 */
@Aspect
@Component
public class ResponseFillerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFillerAspect.class);
    private static final String POINTCUT = "execution(org.springframework.http.ResponseEntity hu.psprog.leaflet.web.rest.controller.*.*(..)) "
            + "and @annotation(hu.psprog.leaflet.web.annotation.FillResponse)";
    private static final String NON_PROCESSABLE_RESPONSE = "Received endpoint response is non-changeable - expected 'ResponseEntity<T extends BaseBodyDataModel>' type.";

    private List<ResponseFiller> responseFillers;

    @Autowired
    public ResponseFillerAspect(List<ResponseFiller> responseFillers) {
        this.responseFillers = responseFillers;
    }

    /**
     * "Around" aspect to modify response.
     * Fillers will be called (if required) to add additional information to the response.
     *
     * @param proceedingJoinPoint join point
     * @return modified response or the original one if response cannot be modified
     * @throws Throwable any exception that can be thrown by the method behind the aspect proxy
     */
    @Around(value = POINTCUT)
    public Object aspectToWrapAnswer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object methodResponse = proceedingJoinPoint.proceed();
        BaseBodyDataModel responseBody;
        try {
            responseBody = extractBody(methodResponse);
        } catch (ClassCastException | IllegalStateException exc) {
            LOGGER.warn("Failed to extract response.", exc);
            return methodResponse;
        }

        WrapperBodyDataModel.Builder<BaseBodyDataModel> builder = WrapperBodyDataModel.Builder.getBuilder();
        addBody(responseBody, builder);
        callFillers(builder);

        return ResponseEntity
                .status(extractStatusCode(methodResponse))
                .body(builder.build());
    }

    private void addBody(BaseBodyDataModel responseBody, WrapperBodyDataModel.Builder<BaseBodyDataModel> builder) {
        if (responseBody instanceof ErrorMessageDataModel) {
            builder.withError((ErrorMessageDataModel) responseBody);
        } else {
            builder.withBody(responseBody);
        }
    }

    private void callFillers(WrapperBodyDataModel.Builder<BaseBodyDataModel> builder) {
        responseFillers.stream()
                .filter(ResponseFiller::shouldFill)
                .forEach(responseFiller -> responseFiller.fill(builder));
    }

    private HttpStatus extractStatusCode(Object methodResponse) {
        return ((ResponseEntity<?>) methodResponse).getStatusCode();
    }

    private BaseBodyDataModel extractBody(Object methodResponse) {
        return Optional.of(methodResponse)
                .map(response -> ((ResponseEntity<?>) response).getBody())
                .filter(BaseBodyDataModel.class::isInstance)
                .map(BaseBodyDataModel.class::cast)
                .orElseThrow(() -> new IllegalStateException(NON_PROCESSABLE_RESPONSE));
    }
}
