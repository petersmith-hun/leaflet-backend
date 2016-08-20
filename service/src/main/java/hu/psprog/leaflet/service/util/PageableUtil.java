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
 * @author Peter Smith
 */
public class PageableUtil {

    private PageableUtil() {
        // utility class - prevent initialization
    }

    public static Pageable createPage(int page, int limit, OrderDirection direction, String orderBy) {

        return new PageRequest(page - 1, limit, direction.getDirection(), orderBy);
    }

    public static <T extends BaseVO, S extends SerializableEntity> EntityPageVO<T> convertPage(Page<S> page, Converter<S, T> converter) {

        Page remappedPage = page.map(converter);
        EntityPageVO<T> convertedPage = new EntityPageVO.Builder()
                .withPageCount(remappedPage.getTotalPages())
                .withPageSize(remappedPage.getSize())
                .withPageNumber(remappedPage.getNumber())
                .withEntityCount(remappedPage.getTotalElements())
                .withEntityCountOnPage(remappedPage.getNumberOfElements())
                .isFirst(remappedPage.isFirst())
                .isLast(remappedPage.isLast())
                .hasNext(remappedPage.hasNext())
                .hasPrevious(remappedPage.hasPrevious())
                .withEntitiesOnPage(remappedPage.getContent())
                .createEntityPageVO();

        return convertedPage;
    }
}
