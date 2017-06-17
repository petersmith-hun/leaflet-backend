package hu.psprog.leaflet.web.rest.filler;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;

/**
 * Interface for response fillers. A response filler extends REST response with specific sections.
 *
 * @author Peter Smith
 */
public interface ResponseFiller {

    /**
     * Fills response with extracted information.
     *
     * @param wrapperBodyDataModelBuilder builder for the wrapper model holding extra information
     */
    void fill(WrapperBodyDataModel.Builder wrapperBodyDataModelBuilder);

    /**
     * Decides whether this filler can be applied or not.
     *
     * @return {@code true} if filler can be applied, {@code false} otherwise
     */
    boolean shouldFill();
}
