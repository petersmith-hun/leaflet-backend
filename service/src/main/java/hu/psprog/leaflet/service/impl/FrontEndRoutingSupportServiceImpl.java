package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.FrontEndRouteDAO;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.repository.specification.FrontEndRouteSpecification;
import hu.psprog.leaflet.service.FrontEndRoutingSupportService;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.support.routing.RouteMaskProcessor;
import hu.psprog.leaflet.service.security.annotation.PermitAdmin;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FrontEndRoutingSupportService}.
 *
 * @author Peter Smith
 */
@Service
public class FrontEndRoutingSupportServiceImpl implements FrontEndRoutingSupportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndRoutingSupportServiceImpl.class);

    private static final String COULD_NOT_PERSIST_FRONT_END_ROUTE = "Could not persist FrontEndRoute";
    private static final String A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS = "A route with the specified ID already exists";

    private FrontEndRouteDAO frontEndRouteDAO;
    private ConversionService conversionService;
    private List<RouteMaskProcessor> routeMaskProcessors;

    @Autowired
    public FrontEndRoutingSupportServiceImpl(FrontEndRouteDAO frontEndRouteDAO, ConversionService conversionService, List<RouteMaskProcessor> routeMaskProcessors) {
        this.frontEndRouteDAO = frontEndRouteDAO;
        this.conversionService = conversionService;
        this.routeMaskProcessors = routeMaskProcessors;
    }

    @Override
    public List<FrontEndRouteVO> getHeaderMenu() {
        return filterPublicRoutesWithSpecification(FrontEndRouteSpecification.IS_HEADER);
    }

    @Override
    public List<FrontEndRouteVO> getFooterMenu() {
        return filterPublicRoutesWithSpecification(FrontEndRouteSpecification.IS_FOOTER);
    }

    @Override
    public List<FrontEndRouteVO> getStandaloneRoutes() {
        return filterPublicRoutesWithSpecification(FrontEndRouteSpecification.IS_STANDALONE);
    }

    @Override
    public List<FrontEndRouteVO> getDynamicRoutes() {

        return frontEndRouteDAO.findAll(buildFilter(FrontEndRouteSpecification.IS_MASK)).stream()
                .map(frontEndRoute -> conversionService.convert(frontEndRoute, FrontEndRouteVO.class))
                .flatMap(frontEndRoute -> routeMaskProcessors.stream()
                        .filter(processor -> processor.supports(frontEndRoute))
                        .flatMap(processor -> processor.process(frontEndRoute).stream()))
                .collect(Collectors.toList());
    }

    @Override
    @PermitAdmin
    public Long createOne(FrontEndRouteVO entity) throws ServiceException {

        FrontEndRoute createdRoute;
        try {
            createdRoute = frontEndRouteDAO.save(conversionService.convert(entity, FrontEndRoute.class));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS, e);
        } catch (Exception exc) {
            throw new ServiceException(COULD_NOT_PERSIST_FRONT_END_ROUTE, exc);
        }

        if (Objects.isNull(createdRoute)) {
            throw new EntityCreationException(FrontEndRoute.class);
        }

        LOGGER.info("New route [{}] has been created with ID [{}]", createdRoute.getRouteId(), createdRoute.getId());

        return createdRoute.getId();
    }

    @Override
    @PermitAdmin
    public void deleteByID(Long id) throws ServiceException {

        assertExisting(id);
        frontEndRouteDAO.delete(id);
        LOGGER.info("Deleted route of ID [{}]", id);
    }

    @Override
    @PermitAdmin
    public FrontEndRouteVO getOne(Long id) throws ServiceException {

        assertExisting(id);

        return conversionService.convert(frontEndRouteDAO.findOne(id), FrontEndRouteVO.class);
    }

    @Override
    @PermitAdmin
    public List<FrontEndRouteVO> getAll() {

        return frontEndRouteDAO.findAll().stream()
                .map(frontEndRoute -> conversionService.convert(frontEndRoute, FrontEndRouteVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return frontEndRouteDAO.count();
    }

    @Override
    @PermitAdmin
    public void enable(Long id) throws EntityNotFoundException {

        assertExisting(id);
        frontEndRouteDAO.enable(id);
        LOGGER.info("Enabled route of ID [{}]", id);
    }

    @Override
    @PermitAdmin
    public void disable(Long id) throws EntityNotFoundException {

        assertExisting(id);
        frontEndRouteDAO.disable(id);
        LOGGER.info("Disabled route of ID [{}]", id);
    }

    @Override
    @PermitAdmin
    public FrontEndRouteVO updateOne(Long id, FrontEndRouteVO updatedEntity) throws ServiceException {

        FrontEndRoute updatedRoute;
        try {
            updatedRoute = frontEndRouteDAO.updateOne(id, conversionService.convert(updatedEntity, FrontEndRoute.class));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(A_ROUTE_WITH_THE_SPECIFIED_ID_ALREADY_EXISTS, e);
        } catch (Exception exc) {
            throw new ServiceException(COULD_NOT_PERSIST_FRONT_END_ROUTE, exc);
        }

        if (Objects.isNull(updatedRoute)) {
            throw new EntityNotFoundException(FrontEndRoute.class, id);
        }

        LOGGER.info("Existing route [{}] with ID [{}] has been updated", updatedRoute.getRouteId(), id);

        return conversionService.convert(updatedRoute, FrontEndRouteVO.class);
    }

    private void assertExisting(Long id) throws EntityNotFoundException {

        if (!frontEndRouteDAO.exists(id)) {
            throw new EntityNotFoundException(FrontEndRoute.class, id);
        }
    }

    private List<FrontEndRouteVO> filterPublicRoutesWithSpecification(Specification<FrontEndRoute> specification) {

        return frontEndRouteDAO.findAll(buildFilter(specification)).stream()
                .map(frontEndRoute -> conversionService.convert(frontEndRoute, FrontEndRouteVO.class))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(FrontEndRouteVO::getSequenceNumber))
                .collect(Collectors.toList());
    }

    private Specification<FrontEndRoute> buildFilter(Specification<FrontEndRoute> specification) {

        return Specification
                .where(FrontEndRouteSpecification.IS_ENABLED)
                .and(specification);
    }
}
