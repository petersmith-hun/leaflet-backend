package hu.psprog.leaflet.web.processor.aspect;

import hu.psprog.leaflet.service.vo.CustomSEODataProviderVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.web.processor.ResponseProcessor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Web MVC response helper aspects.
 *
 * @author Peter Smith
 */
@Aspect
@Component
public class ResponseProcessorAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseProcessorAspect.class);

    private static final String POINTCUT_ENTITY_PAGE = "execution(hu.psprog.leaflet.service.vo.EntityPageVO+ hu.psprog.leaflet.service.*.*(..))";
    private static final String POINTCUT_CUSTOM_SEO_PROVIDER = "execution(hu.psprog.leaflet.service.vo.CustomSEODataProviderVO+ hu.psprog.leaflet.service.*.*(..))";

    private static final String RETURNING_ENTITY_PAGE_VO = "entityPageVO";
    private static final String RETURNING_CUSTOM_SEO_PROVIDER = "customSEODataProviderVO";

    @Autowired
    private ResponseProcessor responseProcessor;

    /**
     * Aspect to handle entity page parameters.
     * Advices execution for all service returning {@link EntityPageVO}.
     *
     * @param entityPageVO returning value of the advised method
     */
    @AfterReturning(
            value = POINTCUT_ENTITY_PAGE,
            returning = RETURNING_ENTITY_PAGE_VO)
    public void aspectToAutoHandleEntityPageParameters(EntityPageVO entityPageVO) {

        LOGGER.info("Aspect activated for entityPageVO={}", entityPageVO);
        responseProcessor.process(entityPageVO);
    }

    /**
     * Aspect to handle SEO parameters returned by custom SEO provider VOs.
     * Advices execution for all service returning any type that extends {@link CustomSEODataProviderVO}.
     *
     * @param customSEODataProviderVO returning value of the advised method
     */
    @AfterReturning(
            value = POINTCUT_CUSTOM_SEO_PROVIDER,
            returning = RETURNING_CUSTOM_SEO_PROVIDER)
    public void aspectToHandleCustomSEODataProviders(CustomSEODataProviderVO customSEODataProviderVO) {

        responseProcessor.process(customSEODataProviderVO);
        LOGGER.info("Aspect activated for customSEODataProvider");
    }
}
