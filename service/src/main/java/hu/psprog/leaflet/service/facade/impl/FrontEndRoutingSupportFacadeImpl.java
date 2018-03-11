package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FrontEndRoutingSupportFacade;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link FrontEndRoutingSupportFacade}.
 *
 * @author Peter Smith
 */
@Service
public class FrontEndRoutingSupportFacadeImpl implements FrontEndRoutingSupportFacade {

    private FrontEndRoutingSupportService frontEndRoutingSupportService;

    @Autowired
    public FrontEndRoutingSupportFacadeImpl(FrontEndRoutingSupportService frontEndRoutingSupportService) {
        this.frontEndRoutingSupportService = frontEndRoutingSupportService;
    }

    @Override
    public List<FrontEndRouteVO> getHeaderMenu() {
        return frontEndRoutingSupportService.getHeaderMenu();
    }

    @Override
    public List<FrontEndRouteVO> getFooterMenu() {
        return frontEndRoutingSupportService.getFooterMenu();
    }

    @Override
    public List<FrontEndRouteVO> getDynamicRoutes() {
        return frontEndRoutingSupportService.getDynamicRoutes();
    }

    @Override
    public FrontEndRouteVO createOne(FrontEndRouteVO entity) throws ServiceException {
        Long createdID = frontEndRoutingSupportService.createOne(entity);
        return frontEndRoutingSupportService.getOne(createdID);
    }

    @Override
    public FrontEndRouteVO getOne(Long id) throws ServiceException {
        return frontEndRoutingSupportService.getOne(id);
    }

    @Override
    public List<FrontEndRouteVO> getAll() {
        return frontEndRoutingSupportService.getAll();
    }

    @Override
    public Long count() {
        return frontEndRoutingSupportService.count();
    }

    @Override
    public FrontEndRouteVO updateOne(Long id, FrontEndRouteVO updatedFrontEndRoute) throws ServiceException {
        frontEndRoutingSupportService.updateOne(id, updatedFrontEndRoute);
        return frontEndRoutingSupportService.getOne(id);
    }

    @Override
    public void deletePermanently(Long id) throws ServiceException {
        frontEndRoutingSupportService.deleteByID(id);
    }

    @Override
    public FrontEndRouteVO changeStatus(Long id) throws ServiceException {

        FrontEndRouteVO current = frontEndRoutingSupportService.getOne(id);
        if (current.isEnabled()) {
            frontEndRoutingSupportService.disable(id);
        } else {
            frontEndRoutingSupportService.enable(id);
        }

        return frontEndRoutingSupportService.getOne(id);
    }
}
