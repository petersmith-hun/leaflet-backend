package hu.psprog.leaflet.web.processor;

import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.CustomSEODataProviderVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;

/**
 * Handles special service answers, extracts and populates corresponding data into request.
 *
 * @author Peter Smith
 */
public interface ResponseProcessor {

    /**
     * Extracts pagination information from {@link EntityPageVO} value object.
     *
     * @param response service response of type {@link EntityPageVO}
     * @param <T> type of wrapped value object
     */
    <T extends BaseVO> void process(EntityPageVO<T> response);

    /**
     * Extracts custom SEO values from {@link CustomSEODataProviderVO} value object.
     *
     * @param response service response of type {@link CustomSEODataProviderVO}
     * @param <T> type of wrapped value object
     */
    <T extends BaseVO> void process(CustomSEODataProviderVO<T> response);
}
