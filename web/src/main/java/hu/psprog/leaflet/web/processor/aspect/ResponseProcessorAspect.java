package hu.psprog.leaflet.web.processor.aspect;

import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.CustomSEODataProviderVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.web.processor.impl.CustomSEODataProviderResponseProcessor;
import hu.psprog.leaflet.web.processor.impl.EntityPageResponseProcessor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Web MVC response helper aspects.
 *
 * @author Peter Smith
 */
@Aspect
@Component
class ResponseProcessorAspect {

    private static final String POINTCUT_ENTITY_PAGE = "execution(hu.psprog.leaflet.service.vo.EntityPageVO+ hu.psprog.leaflet.service.*.*(..))";
    private static final String POINTCUT_CUSTOM_SEO_PROVIDER = "execution(hu.psprog.leaflet.service.vo.CustomSEODataProviderVO+ hu.psprog.leaflet.service.*.*(..))";

    private static final String RETURNING_ENTITY_PAGE_VO = "entityPageVO";
    private static final String RETURNING_CUSTOM_SEO_PROVIDER = "customSEODataProviderVO";

    private CustomSEODataProviderResponseProcessor customSEODataProviderResponseProcessor;
    private EntityPageResponseProcessor entityPageResponseProcessor;

    @Autowired
    public ResponseProcessorAspect(CustomSEODataProviderResponseProcessor customSEODataProviderResponseProcessor,
                                   EntityPageResponseProcessor entityPageResponseProcessor) {
        this.customSEODataProviderResponseProcessor = customSEODataProviderResponseProcessor;
        this.entityPageResponseProcessor = entityPageResponseProcessor;
    }

    /**
     * Aspect to handle entity page parameters.
     * Advices execution for all service returning {@link EntityPageVO}.
     *
     * @param entityPageVO returning value of the advised method
     */
    @AfterReturning(
            value = POINTCUT_ENTITY_PAGE,
            returning = RETURNING_ENTITY_PAGE_VO)
    void aspectToAutoHandleEntityPageParameters(EntityPageVO<? extends BaseVO> entityPageVO) {
        entityPageResponseProcessor.process(entityPageVO);
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
    void aspectToHandleCustomSEODataProviders(CustomSEODataProviderVO<? extends BaseVO> customSEODataProviderVO) {
        customSEODataProviderResponseProcessor.process(customSEODataProviderVO);
    }
}
