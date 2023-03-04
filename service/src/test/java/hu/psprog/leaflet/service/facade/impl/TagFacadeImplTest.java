package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TagFacadeImpl}.
 * 
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagFacadeImplTest {

    private static final long TAG_ID = 4L;
    private static final TagVO TAG_VO = TagVO.getBuilder()
            .withId(TAG_ID)
            .build();
    private static final long ENTRY_ID = 8L;
    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(ENTRY_ID);
    private static final TagAssignmentVO TAG_ASSIGNMENT_VO = TagAssignmentVO.getBuilder()
            .withEntryID(ENTRY_ID)
            .withTagID(TAG_ID)
            .build();
    
    @Mock
    private TagService tagService;

    @Mock
    private EntryService entryService;
    
    @InjectMocks
    private TagFacadeImpl tagFacade;

    @Test
    public void shouldGetOne() throws ServiceException {

        // when
        tagFacade.getOne(TAG_ID);

        // then
        verify(tagService).getOne(TAG_ID);
    }

    @Test
    public void shouldGetAll() {

        // when
        tagFacade.getAll();

        // then
        verify(tagService).getAll();
    }

    @Test
    public void shouldGetPublicTags() {

        // when
        tagFacade.getPublicTags();

        // then
        verify(tagService).getPublicTags();
    }

    @Test
    public void shouldCreateOne() throws ServiceException {

        // given
        given(tagService.createOne(TAG_VO)).willReturn(TAG_ID);

        // when
        tagFacade.createOne(TAG_VO);

        // then
        verify(tagService).createOne(TAG_VO);
        verify(tagService).getOne(TAG_ID);
    }

    @Test
    public void shouldUpdateOne() throws ServiceException {

        // when
        tagFacade.updateOne(TAG_ID, TAG_VO);

        // then
        verify(tagService).updateOne(TAG_ID, TAG_VO);
        verify(tagService).getOne(TAG_ID);
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        tagFacade.deletePermanently(TAG_ID);

        // then
        verify(tagService).deleteByID(TAG_ID);
    }

    @Test
    public void shouldChangeStatusToEnabled() throws ServiceException {

        // given
        given(tagService.getOne(TAG_ID)).willReturn(TAG_VO);

        // when
        tagFacade.changeStatus(TAG_ID);

        // then
        verify(tagService).enable(TAG_ID);
    }

    @Test
    public void shouldChangeStatusToDisabled() throws ServiceException {

        // given
        given(tagService.getOne(TAG_ID)).willReturn(TagVO.getBuilder()
                .withEnabled(true)
                .build());

        // when
        tagFacade.changeStatus(TAG_ID);

        // then
        verify(tagService).disable(TAG_ID);
    }

    @Test
    public void shouldAttachTagToEntry() throws ServiceException {

        // given
        givenTagAndEntryInfo();

        // when
        tagFacade.attachTagToEntry(TAG_ASSIGNMENT_VO);

        // then
        verify(tagService).attachTagToEntry(TAG_VO, ENTRY_VO);
    }

    @Test
    public void shouldDetachTagFromEntry() throws ServiceException {

        // given
        givenTagAndEntryInfo();

        // when
        tagFacade.detachTagFromEntry(TAG_ASSIGNMENT_VO);

        // then
        verify(tagService).detachTagFromEntry(TAG_VO, ENTRY_VO);
    }

    private void givenTagAndEntryInfo() throws ServiceException {
        given(entryService.getOne(ENTRY_ID)).willReturn(ENTRY_VO);
        given(tagService.getOne(TAG_ID)).willReturn(TAG_VO);
    }
}