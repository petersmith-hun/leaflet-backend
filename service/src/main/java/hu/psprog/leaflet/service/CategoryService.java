package hu.psprog.leaflet.service;

import hu.psprog.leaflet.service.crud.CreateOperationCapableService;
import hu.psprog.leaflet.service.crud.DeleteOperationCapableService;
import hu.psprog.leaflet.service.crud.ReadOperationCapableService;
import hu.psprog.leaflet.service.crud.StatusChangeCapableService;
import hu.psprog.leaflet.service.crud.UpdateOperationCapableService;
import hu.psprog.leaflet.service.vo.CategoryVO;

import java.util.List;

/**
 * Category service operations interface.
 *
 * @author Peter Smith
 */
public interface CategoryService extends CreateOperationCapableService<CategoryVO, Long>,
        ReadOperationCapableService<CategoryVO, Long>,
        UpdateOperationCapableService<CategoryVO, CategoryVO, Long>,
        DeleteOperationCapableService<CategoryVO, Long>,
        StatusChangeCapableService<Long> {

    List<CategoryVO> getAllPublic();
}
