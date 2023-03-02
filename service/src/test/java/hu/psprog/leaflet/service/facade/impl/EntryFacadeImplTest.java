package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryFacadeImpl}.
 * 
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntryFacadeImplTest {

    private static final long ENTRY_ID = 8L;
    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(ENTRY_ID);
    private static final int PAGE = 1;
    private static final int LIMIT = 10;
    private static final String DIRECTION = "DESC";
    private static final String ORDER_BY = "TITLE";
    private static final String NON_EXISTING = "non-existing";
    private static final long CATEGORY_ID = 9L;
    private static final long TAG_ID = 8L;
    private static final CategoryVO CATEGORY_VO = CategoryVO.wrapMinimumVO(CATEGORY_ID);
    private static final TagVO TAG_VO = TagVO.wrapMinimumVO(TAG_ID);
    private static final String CONTENT = "content";

    @Mock
    private EntryService entryService;
    
    @InjectMocks
    private EntryFacadeImpl entryFacade;

    @Test
    public void shouldGetOne() throws ServiceException {

        // when
        entryFacade.getOne(ENTRY_ID);

        // then
        verify(entryService).getOne(ENTRY_ID);
    }

    @Test
    public void shouldFindByLink() throws EntityNotFoundException {

        // given
        String link = "link";

        // when
        entryFacade.findByLink(link);

        // then
        verify(entryService).findByLink(link);
    }

    @Test
    public void shouldGetAll() {

        // when
        entryFacade.getAll();

        // then
        verify(entryService).getAll();
    }

    @Test
    public void shouldGetListOfPublicEntries() {

        // when
        entryFacade.getListOfPublicEntries();

        // then
        verify(entryService).getListOfPublicEntries();
    }

    @Test
    public void shouldCreateOne() throws ServiceException {

        // given
        given(entryService.createOne(ENTRY_VO)).willReturn(ENTRY_ID);

        // when
        entryFacade.createOne(ENTRY_VO);

        // then
        verify(entryService).createOne(ENTRY_VO);
        verify(entryService).getOne(ENTRY_ID);
    }

    @Test
    public void shouldUpdateOne() throws ServiceException {

        // when
        entryFacade.updateOne(ENTRY_ID, ENTRY_VO);

        // then
        verify(entryService).updateOne(ENTRY_ID, ENTRY_VO);
        verify(entryService).getOne(ENTRY_ID);
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        entryFacade.deletePermanently(ENTRY_ID);

        // then
        verify(entryService).deleteByID(ENTRY_ID);
    }

    @Test
    public void shouldChangeStatusToEnabled() throws ServiceException {

        // given
        given(entryService.getOne(ENTRY_ID)).willReturn(ENTRY_VO);

        // when
        entryFacade.changeStatus(ENTRY_ID);

        // then
        verify(entryService).enable(ENTRY_ID);
    }

    @Test
    public void shouldChangeStatusToDisabled() throws ServiceException {

        // given
        given(entryService.getOne(ENTRY_ID)).willReturn(EntryVO.getBuilder()
                .withEnabled(true)
                .build());

        // when
        entryFacade.changeStatus(ENTRY_ID);

        // then
        verify(entryService).disable(ENTRY_ID);
    }

    @Test
    public void shouldGetEntityPage() {

        // when
        entryFacade.getEntityPage(PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(entryService).getEntityPage(PAGE, LIMIT, OrderDirection.DESC, EntryVO.OrderBy.TITLE);
    }
    
    @Test
    public void shouldGetEntityPageWithFallbackPagingParameters() {

        // when
        entryFacade.getEntityPage(PAGE, LIMIT, NON_EXISTING, NON_EXISTING);

        // then
        verify(entryService).getEntityPage(PAGE, LIMIT, OrderDirection.ASC, EntryVO.OrderBy.CREATED);
    }

    @Test
    public void shouldGetPageOfPublicEntries() {

        // when
        entryFacade.getPageOfPublicEntries(PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(entryService).getPageOfPublicEntries(PAGE, LIMIT, OrderDirection.DESC, EntryVO.OrderBy.TITLE);
    }

    @Test
    public void shouldGetPageOfPublicEntriesUnderCategory() {

        // when
        entryFacade.getPageOfPublicEntriesUnderCategory(CATEGORY_ID, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(entryService).getPageOfPublicEntriesUnderCategory(CATEGORY_VO, PAGE, LIMIT, OrderDirection.DESC, EntryVO.OrderBy.TITLE);
    }

    @Test
    public void shouldGetPageOfPublicEntriesUnderTag() {

        // when
        entryFacade.getPageOfPublicEntriesUnderTag(TAG_ID, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(entryService).getPageOfPublicEntriesUnderTag(TAG_VO, PAGE, LIMIT, OrderDirection.DESC, EntryVO.OrderBy.TITLE);
    }

    @Test
    public void shouldGetPageOfPublicEntriesByContent() {

        // when
        entryFacade.getPageOfPublicEntriesByContent(CONTENT, PAGE, LIMIT, DIRECTION, ORDER_BY);

        // then
        verify(entryService).getPageOfPublicEntriesByContent(CONTENT, PAGE, LIMIT, OrderDirection.DESC, EntryVO.OrderBy.TITLE);
    }
}