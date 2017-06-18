package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.persistence.entity.SerializableEntity;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.vo.BaseVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Utility for creating {@link Pageable} page configurations and converting {@link Page} to {@link EntityPageVO} objects.
 *
 * @author Peter Smith
 */
public class PageableUtil {

    private PageableUtil() {
        // utility class - prevent initialization
    }

    /**
     * Creates a {@link Pageable} configuration.
     *
     * @param page page number (page >= 1)
     * @param limit maximum number of items on one page
     * @param direction order direction
     * @param orderBy order by field
     * @return Pageable configuration
     */
    public static Pageable createPage(int page, int limit, OrderDirection direction, String orderBy) {

        return new PageRequest(page - 1, limit, direction.getDirection(), orderBy);
    }

    /**
     * Converts {@link Page} to {@link EntityPageVO}.
     *
     * @param page source {@link Page} object
     * @param converter converter of type {@link Converter} to use for conversion
     * @param <T> destination {@link BaseVO} type
     * @param <S> source {@link SerializableEntity} type
     * @return converter page
     */
    public static <T extends BaseVO, S extends SerializableEntity> EntityPageVO<T> convertPage(Page<S> page, Converter<S, T> converter, Class<T> targetClass) {

        Page<T> remappedPage = page.map(converter);

        return EntityPageVO.getBuilder()
                .withPageCount(remappedPage.getTotalPages())
                .withPageSize(remappedPage.getSize())
                .withPageNumber(remappedPage.getNumber() + 1) // Page would count page number from 0, so let's increase by 1
                .withEntityCount(remappedPage.getTotalElements())
                .withEntityCountOnPage(remappedPage.getNumberOfElements())
                .withFirst(remappedPage.isFirst())
                .withLast(remappedPage.isLast())
                .withHasNext(remappedPage.hasNext())
                .withHasPrevious(remappedPage.hasPrevious())
                .withEntitiesOnPage(remappedPage.getContent())
                .build(targetClass);
    }
}
