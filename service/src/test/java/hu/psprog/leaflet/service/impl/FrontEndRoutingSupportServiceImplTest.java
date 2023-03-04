package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.FrontEndRouteDAO;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.service.converter.FrontEndRouteToFrontEndRouteVOConverter;
import hu.psprog.leaflet.service.converter.FrontEndRouteVOToFrontEndRouteConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.support.routing.RouteMaskProcessor;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FrontEndRoutingSupportServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FrontEndRoutingSupportServiceImplTest {

    private static final Long CONTROL_ID = 1L;

    private static final FrontEndRoute FRONT_END_ROUTE_1 = prepareFrontEndRoute(1);
    private static final FrontEndRoute FRONT_END_ROUTE_2 = prepareFrontEndRoute(2);
    private static final FrontEndRoute FRONT_END_ROUTE_3 = prepareFrontEndRoute(3);

    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_1 = prepareFrontEndRouteVO(1);
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_2 = prepareFrontEndRouteVO(2);
    private static final FrontEndRouteVO FRONT_END_ROUTE_VO_3 = prepareFrontEndRouteVO(3);

    @Mock(lenient = true)
    private FrontEndRouteDAO frontEndRouteDAO;

    @Mock(lenient = true)
    private RouteMaskProcessor firstRouteMaskProcessor;

    @Mock(lenient = true)
    private RouteMaskProcessor secondRouteMaskProcessor;

    @Mock(lenient = true)
    private FrontEndRouteToFrontEndRouteVOConverter frontEndRouteToFrontEndRouteVOConverter;

    @Mock(lenient = true)
    private FrontEndRouteVOToFrontEndRouteConverter frontEndRouteVOToFrontEndRouteConverter;

    @Mock
    private Root<FrontEndRoute> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @Captor
    private ArgumentCaptor<Specification<FrontEndRoute>> filterCaptor;

    private FrontEndRoutingSupportServiceImpl frontEndRoutingSupportService;

    @BeforeEach
    public void setup() {

        given(frontEndRouteToFrontEndRouteVOConverter.convert(FRONT_END_ROUTE_1)).willReturn(FRONT_END_ROUTE_VO_1);
        given(frontEndRouteToFrontEndRouteVOConverter.convert(FRONT_END_ROUTE_2)).willReturn(FRONT_END_ROUTE_VO_2);
        given(frontEndRouteToFrontEndRouteVOConverter.convert(FRONT_END_ROUTE_3)).willReturn(FRONT_END_ROUTE_VO_3);
        given(frontEndRouteDAO.findAll(any(Specification.class))).willReturn(Arrays.asList(FRONT_END_ROUTE_2, FRONT_END_ROUTE_3, FRONT_END_ROUTE_1));

        frontEndRoutingSupportService = new FrontEndRoutingSupportServiceImpl(frontEndRouteDAO, Arrays.asList(firstRouteMaskProcessor, secondRouteMaskProcessor),
                frontEndRouteToFrontEndRouteVOConverter, frontEndRouteVOToFrontEndRouteConverter);
    }

    @Test
    public void shouldGetHeaderMenu() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getHeaderMenu();

        // then
        assertResultForStaticRouteList(result);
        verifyFilter(FrontEndRouteType.HEADER_MENU);
    }

    @Test
    public void shouldGetFooterMenu() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getFooterMenu();

        // then
        assertResultForStaticRouteList(result);
        verifyFilter(FrontEndRouteType.FOOTER_MENU);
    }

    @Test
    public void shouldGetStandaloneRoutes() {

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getStandaloneRoutes();

        // then
        assertResultForStaticRouteList(result);
        verifyFilter(FrontEndRouteType.STANDALONE);
    }

    @Test
    public void shouldGetDynamicRoutes() {

        // given
        prepareContextForDynamicRoutesTest();

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getDynamicRoutes();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(6));
        assertThat(result.stream()
                .map(FrontEndRouteVO::getId)
                .collect(Collectors.toList()).containsAll(Arrays.asList(4L, 5L, 8L, 9L, 12L, 13L)), is(true));
        verifyFilter(FrontEndRouteType.ENTRY_ROUTE_MASK, FrontEndRouteType.CATEGORY_ROUTE_MASK);
    }

    @Test
    public void shouldCreate() throws ServiceException {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        given(frontEndRouteDAO.save(FRONT_END_ROUTE_1)).willReturn(FRONT_END_ROUTE_1);

        // when
        Long result = frontEndRoutingSupportService.createOne(FRONT_END_ROUTE_VO_1);

        // then
        assertThat(result, equalTo(1L));
    }

    @Test
    public void shouldCreateFailWithConstraintViolationException() {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        doThrow(DataIntegrityViolationException.class).when(frontEndRouteDAO).save(any());

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> frontEndRoutingSupportService.createOne(FRONT_END_ROUTE_VO_1));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateFailWithServiceException() {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        doThrow(RuntimeException.class).when(frontEndRouteDAO).save(any());

        // when
        Assertions.assertThrows(ServiceException.class, () -> frontEndRoutingSupportService.createOne(FRONT_END_ROUTE_VO_1));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdate() throws ServiceException {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        given(frontEndRouteDAO.updateOne(CONTROL_ID, FRONT_END_ROUTE_1)).willReturn(Optional.of(FRONT_END_ROUTE_1));

        // when
        FrontEndRouteVO result = frontEndRoutingSupportService.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO_1);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO_1));
    }

    @Test
    public void shouldUpdateFailWithEntityNotFoundException() {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> frontEndRoutingSupportService.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO_1));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateFailWithConstraintViolationException() {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        doThrow(DataIntegrityViolationException.class).when(frontEndRouteDAO).updateOne(anyLong(), any());

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> frontEndRoutingSupportService.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO_1));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateFailWithServiceException() {

        // given
        given(frontEndRouteVOToFrontEndRouteConverter.convert(FRONT_END_ROUTE_VO_1)).willReturn(FRONT_END_ROUTE_1);
        doThrow(RuntimeException.class).when(frontEndRouteDAO).updateOne(anyLong(), any());

        // when
        Assertions.assertThrows(ServiceException.class, () -> frontEndRoutingSupportService.updateOne(CONTROL_ID, FRONT_END_ROUTE_VO_1));

        // then
        // exception expected
    }

    @Test
    public void shouldDelete() throws ServiceException {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(true);

        // when
        frontEndRoutingSupportService.deleteByID(CONTROL_ID);

        // then
        verify(frontEndRouteDAO).delete(CONTROL_ID);
    }

    @Test
    public void shouldDeleteFailWhenRouteDoesNotExist() {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> frontEndRoutingSupportService.deleteByID(CONTROL_ID));

        // then
        // expected exception
    }

    @Test
    public void shouldGetOne() throws ServiceException {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(true);
        given(frontEndRouteDAO.findById(CONTROL_ID)).willReturn(Optional.of(FRONT_END_ROUTE_1));

        // when
        FrontEndRouteVO result = frontEndRoutingSupportService.getOne(CONTROL_ID);

        // then
        assertThat(result, equalTo(FRONT_END_ROUTE_VO_1));
    }

    @Test
    public void shouldGetOneFailWhenRouteDoesNotExist() {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> frontEndRoutingSupportService.getOne(CONTROL_ID));

        // then
        // expected exception
    }

    @Test
    public void shouldGetAll() {

        // given
        given(frontEndRouteDAO.findAll()).willReturn(Arrays.asList(FRONT_END_ROUTE_1, FRONT_END_ROUTE_2, FRONT_END_ROUTE_3));

        // when
        List<FrontEndRouteVO> result = frontEndRoutingSupportService.getAll();

        // then
        assertThat(result.containsAll(Arrays.asList(FRONT_END_ROUTE_VO_1, FRONT_END_ROUTE_VO_2, FRONT_END_ROUTE_VO_3)), is(true));
    }

    @Test
    public void shouldEnable() throws EntityNotFoundException {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(true);

        // when
        frontEndRoutingSupportService.enable(CONTROL_ID);

        // then
        verify(frontEndRouteDAO).enable(CONTROL_ID);
    }

    @Test
    public void shouldEnableFailWhenRouteDoesNotExist() {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> frontEndRoutingSupportService.enable(CONTROL_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldDisable() throws EntityNotFoundException {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(true);

        // when
        frontEndRoutingSupportService.disable(CONTROL_ID);

        // then
        verify(frontEndRouteDAO).disable(CONTROL_ID);
    }

    @Test
    public void shouldDisableFailWhenRouteDoesNotExist() {

        // given
        given(frontEndRouteDAO.exists(CONTROL_ID)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> frontEndRoutingSupportService.disable(CONTROL_ID));

        // then
        // exception expected
    }

    private void verifyFilter(FrontEndRouteType... types) {
        verify(frontEndRouteDAO).findAll(filterCaptor.capture());
        filterCaptor.getValue().toPredicate(root, query, builder);
        Arrays.stream(types).forEach(type -> verify(builder).equal(null, type));
        verify(builder).equal(null, true);
    }

    private void assertResultForStaticRouteList(List<FrontEndRouteVO> result) {
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0), equalTo(FRONT_END_ROUTE_VO_1));
        assertThat(result.get(1), equalTo(FRONT_END_ROUTE_VO_2));
        assertThat(result.get(2), equalTo(FRONT_END_ROUTE_VO_3));
    }

    private void prepareContextForDynamicRoutesTest() {
        given(firstRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_1)).willReturn(true);
        given(firstRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_2)).willReturn(false);
        given(firstRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_3)).willReturn(true);

        given(secondRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_1)).willReturn(false);
        given(secondRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_2)).willReturn(true);
        given(secondRouteMaskProcessor.supports(FRONT_END_ROUTE_VO_3)).willReturn(false);

        given(firstRouteMaskProcessor.process(FRONT_END_ROUTE_VO_1)).willReturn(Arrays.asList(prepareFrontEndRouteVO(4), prepareFrontEndRouteVO(5)));
        given(firstRouteMaskProcessor.process(FRONT_END_ROUTE_VO_2)).willReturn(Arrays.asList(prepareFrontEndRouteVO(6), prepareFrontEndRouteVO(7)));
        given(firstRouteMaskProcessor.process(FRONT_END_ROUTE_VO_3)).willReturn(Arrays.asList(prepareFrontEndRouteVO(8), prepareFrontEndRouteVO(9)));

        given(secondRouteMaskProcessor.process(FRONT_END_ROUTE_VO_1)).willReturn(Arrays.asList(prepareFrontEndRouteVO(10), prepareFrontEndRouteVO(11)));
        given(secondRouteMaskProcessor.process(FRONT_END_ROUTE_VO_2)).willReturn(Arrays.asList(prepareFrontEndRouteVO(12), prepareFrontEndRouteVO(13)));
        given(secondRouteMaskProcessor.process(FRONT_END_ROUTE_VO_3)).willReturn(Arrays.asList(prepareFrontEndRouteVO(14), prepareFrontEndRouteVO(15)));
    }

    private static FrontEndRouteVO prepareFrontEndRouteVO(int id) {
        return FrontEndRouteVO.getBuilder()
                .withId((long) id)
                .withSequenceNumber(id)
                .build();
    }

    private static FrontEndRoute prepareFrontEndRoute(int id) {
        return FrontEndRoute.getBuilder()
                .withId((long) id)
                .withSequenceNumber(id)
                .build();
    }
}