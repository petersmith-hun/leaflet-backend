package hu.psprog.leaflet.web.processor.aspect;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.CategoryVOToCategoryConverter;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.converter.TagVOToTagConverter;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.EntryServiceImpl;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.util.PublishHandler;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.web.processor.impl.CustomSEODataProviderResponseProcessor;
import hu.psprog.leaflet.web.processor.impl.EntityPageResponseProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ResponseProcessorAspect}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ResponseProcessorAspectTest {

    private static final long ENTRY_ID = 1L;
    private static final String ENTRY_LINK = "link";
    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(ENTRY_ID);
    private static final Entry ENTRY = Entry.getBuilder()
            .withId(ENTRY_ID)
            .build();

    @Mock
    private CustomSEODataProviderResponseProcessor customSEODataProviderResponseProcessor;

    @Mock
    private EntityPageResponseProcessor entityPageResponseProcessor;

    @Mock
    private EntryDAO entryDAO;

    @Mock
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @Mock
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Mock
    private CategoryVOToCategoryConverter categoryVOToCategoryConverter;

    @Mock
    private TagVOToTagConverter tagVOToTagConverter;

    @Mock
    private PublishHandler publishHandler;

    @Mock
    private SearchHandler<EntrySearchParametersVO, Entry> searchHandler;

    @InjectMocks
    private ResponseProcessorAspect responseProcessorAspect;

    @Test
    public void shouldHandlePageParameters() {

        // given
        given(entryDAO.findAll(any(), any())).willReturn(new PageImpl<>(Collections.singletonList(ENTRY)));
        given(entryToEntryVOConverter.convert(any())).willReturn(ENTRY_VO);

        // when
        EntityPageVO<EntryVO> result = preparePointcut().getPageOfPublicEntries(1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        verify(entityPageResponseProcessor).process(result);
    }

    @Test
    public void shouldNotActivatePageHandlerPointcut() throws ServiceException {

        // given
        given(entryDAO.findById(ENTRY_ID)).willReturn(Optional.of(ENTRY));
        given(entryToEntryVOConverter.convert(any())).willReturn(ENTRY_VO);

        // when
        preparePointcut().getOne(ENTRY_ID);

        // then
        verifyNoInteractions(entityPageResponseProcessor);
    }

    @Test
    public void shouldHandleCustomSEODataProvider() throws EntityNotFoundException {

        // given
        given(entryDAO.findByLink(ENTRY_LINK)).willReturn(Optional.of(ENTRY));
        given(entryToEntryVOConverter.convert(any())).willReturn(ENTRY_VO);

        // when
        EntryVO result = preparePointcut().findByLink(ENTRY_LINK);

        // then
        verify(customSEODataProviderResponseProcessor).process(result);
    }

    @Test
    public void shouldNotActivateCustomSEODataHandlerPointcut() {

        // given
        given(entryDAO.findAll()).willReturn(Collections.singletonList(ENTRY));
        given(entryToEntryVOConverter.convert(any())).willReturn(ENTRY_VO);

        // when
        preparePointcut().getAll();

        // then
        verifyNoInteractions(customSEODataProviderResponseProcessor);
    }

    private EntryService preparePointcut() {

        AspectJProxyFactory factory = new AspectJProxyFactory(prepareEntryServiceMock());
        factory.addAspect(responseProcessorAspect);

        return factory.getProxy();
    }

    private EntryService prepareEntryServiceMock() {
        return new EntryServiceImpl(entryDAO, entryToEntryVOConverter, entryVOToEntryConverter, categoryVOToCategoryConverter, tagVOToTagConverter, publishHandler, searchHandler);
    }
}