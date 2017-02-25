package hu.psprog.leaflet.service.vo;

/**
 * Ensures that a value object can provide custom SEO values.
 *
 * @author Peter Smith
 */
public interface CustomSEODataProviderVO<T extends BaseVO> {

    /**
     * Returns custom SEO title provided by the entity.
     *
     * @return SEO title
     */
    String getSEOTitle();

    /**
     * Returns custom SEO description provided by the entity.
     *
     * @return SEO description
     */
    String getSEODescription();

    /**
     * Returns custom SEO keywords provided by the entity.
     *
     * @return SEO keywords
     */
    String getSEOKeywords();

    /**
     * Returns parent entity.
     *
     * @return parent entity of {@link BaseVO}
     */
    T getEntity();
}
