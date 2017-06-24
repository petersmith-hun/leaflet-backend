package hu.psprog.leaflet.web.processor;

/**
 * Handles special service answers, extracts and populates corresponding data into request.
 *
 * @param <S> source response type to process
 * @author Peter Smith
 */
@FunctionalInterface
public interface ResponseProcessor<S> {

    /**
     * Extracts required information from response object of type S
     *
     * @param response service response of type S
     */
    void process(S response);
}
