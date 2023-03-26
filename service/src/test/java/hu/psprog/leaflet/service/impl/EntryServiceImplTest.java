package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.specification.EntrySpecification;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.EntryToEntryVOConverter;
import hu.psprog.leaflet.service.converter.EntryVOToEntryConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.InvalidTransitionException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.search.SearchHandler;
import hu.psprog.leaflet.service.util.PublishHandler;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntrySearchParametersVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryServiceImpl} class.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntryServiceImplTest {

    private static final EntryVO ENTRY_VO = EntryVO.wrapMinimumVO(1L);
    private static final CategoryVO CATEGORY_VO = CategoryVO.wrapMinimumVO(2L);
    private static final TagVO TAG_VO = TagVO.wrapMinimumVO(3L);

    @Mock(lenient = true)
    private EntryDAO entryDAO;

    @Mock
    private EntryVOToEntryConverter entryVOToEntryConverter;

    @Mock
    private EntryToEntryVOConverter entryToEntryVOConverter;

    @Mock
    private Entry entry;

    @Mock
    private EntryVO entryVO;

    @Mock
    private PublishHandler publishHandler;

    @Mock
    private SearchHandler<EntrySearchParametersVO, Entry> searchHandler;

    @InjectMocks
    private EntryServiceImpl entryService;

    @Test
    public void testGetOneWithExistingEntry() throws ServiceException {

        // given
        Long id = 1L;
        given(entryDAO.findById(id)).willReturn(Optional.of(entry));
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        EntryVO result = entryService.getOne(id);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryDAO).findById(id);
        verify(entryToEntryVOConverter).convert(entry);
    }

    @Test
    public void testGetOneWithNonExistingEntry() {

        // given
        Long id = 1L;
        given(entryDAO.findById(id)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.getOne(id));

        // then
        // expected exception
        verify(entryDAO).findById(id);
        verify(entryToEntryVOConverter, never()).convert(any());
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<EntryVO> documentVOList = Arrays.asList(entryVO, entryVO, entryVO);
        given(entryDAO.findAll()).willReturn(Arrays.asList(entry, entry, entry));
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        List<EntryVO> result = entryService.getAll();

        // then
        assertThat(result, equalTo(documentVOList));
        verify(entryDAO).findAll();
        verify(entryToEntryVOConverter, times(3)).convert(entry);
    }

    @Test
    public void testGetEntityPage() {

        // given
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);
        given(entryDAO.findAll(any(Pageable.class))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getEntityPage(1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntries() {

        // given
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.Direction.ASC, "published", "created");
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        Specification<Entry> specification = EntrySpecification.IS_PUBLIC;
        EntrySearchParametersVO expectedSearchParameters = EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC))
                .page(1)
                .limit(10)
                .orderDirection(OrderDirection.ASC)
                .orderBy(EntryVO.OrderBy.PUBLISHED)
                .build();

        given(searchHandler.createSpecification(expectedSearchParameters)).willReturn(specification);
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);
        given(entryDAO.findAll(same(specification), eq(expectedPageable))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntries(1, 10, OrderDirection.ASC, EntryVO.OrderBy.PUBLISHED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntriesUnderCategory() {

        // given
        Category category = Category.getBuilder().withId(CATEGORY_VO.getId()).build();
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.Direction.ASC, "created");
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        Specification<Entry> specification = EntrySpecification.isUnderCategory(category);
        EntrySearchParametersVO expectedSearchParameters = EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC))
                .page(1)
                .limit(10)
                .orderDirection(OrderDirection.ASC)
                .orderBy(EntryVO.OrderBy.CREATED)
                .categoryID(Optional.of(CATEGORY_VO.getId()))
                .build();

        given(searchHandler.createSpecification(expectedSearchParameters)).willReturn(specification);
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);
        given(entryDAO.findAll(same(specification), eq(expectedPageable))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntriesUnderCategory(CATEGORY_VO,1, 10, OrderDirection.ASC, EntryVO.OrderBy.CREATED);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntriesUnderTag() {

        // given
        Tag tag = Tag.getBuilder().withId(TAG_VO.getId()).build();
        Pageable expectedPageable = PageRequest.of(4, 20, Sort.Direction.DESC, "title");
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        Specification<Entry> specification = EntrySpecification.isUnderTag(tag);
        EntrySearchParametersVO expectedSearchParameters = EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC))
                .page(5)
                .limit(20)
                .orderDirection(OrderDirection.DESC)
                .orderBy(EntryVO.OrderBy.TITLE)
                .tagID(Optional.of(TAG_VO.getId()))
                .build();

        given(searchHandler.createSpecification(expectedSearchParameters)).willReturn(specification);
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);
        given(entryDAO.findAll(same(specification), eq(expectedPageable))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntriesUnderTag(TAG_VO,5, 20, OrderDirection.DESC, EntryVO.OrderBy.TITLE);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testGetPageOfPublicEntriesByContent() {

        // given
        Pageable expectedPageable = PageRequest.of(2, 25, Sort.Direction.ASC, "id");
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        Specification<Entry> specification = EntrySpecification.containsExpression("content");
        EntrySearchParametersVO expectedSearchParameters = EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC))
                .page(3)
                .limit(25)
                .orderDirection(OrderDirection.ASC)
                .orderBy(EntryVO.OrderBy.ID)
                .content(Optional.of("content"))
                .build();

        given(searchHandler.createSpecification(expectedSearchParameters)).willReturn(specification);
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);
        given(entryDAO.findAll(same(specification), eq(expectedPageable))).willReturn(entryPage);

        // when
        EntityPageVO<EntryVO> result = entryService.getPageOfPublicEntriesByContent("content",3, 25, OrderDirection.ASC, EntryVO.OrderBy.ID);

        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void shouldSearchEntries() {

        // given
        EntrySearchParametersVO entrySearchParametersVO = EntrySearchParametersVO.builder()
                .page(2)
                .limit(30)
                .orderDirection(OrderDirection.DESC)
                .orderBy(EntryVO.OrderBy.TITLE)
                .build();
        Pageable expectedPageable = PageRequest.of(1, 30, Sort.Direction.DESC, "title");
        Page<Entry> entryPage = new PageImpl<>(Collections.singletonList(entry));
        Specification<Entry> specification = Specification.where(null);

        given(searchHandler.createSpecification(entrySearchParametersVO)).willReturn(specification);
        given(entryDAO.findAll(same(specification), eq(expectedPageable))).willReturn(entryPage);
        given(entryToEntryVOConverter.convert(any(Entry.class))).willReturn(ENTRY_VO);

        // when
        EntityPageVO<EntryVO> result = entryService.searchEntries(entrySearchParametersVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getEntitiesOnPage().size(), equalTo(1));
    }

    @Test
    public void testGetListOfPublicEntries() {

        // given
        Specification<Entry> specification = EntrySpecification.IS_PUBLIC;
        EntrySearchParametersVO entrySearchParametersVO = EntrySearchParametersVO.builder()
                .enabled(Optional.of(true))
                .status(Optional.of(EntryStatus.PUBLIC))
                .build();

        given(searchHandler.createSpecification(entrySearchParametersVO)).willReturn(specification);
        given(entryDAO.findAll(same(specification), eq(Pageable.unpaged()))).willReturn(new PageImpl<>(Collections.singletonList(entry)));
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        List<EntryVO> result = entryService.getListOfPublicEntries();

        // then
        assertThat(result.size(), equalTo(1));
        assertThat(result.contains(entryVO), is(true));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryDAO.save(entry)).willReturn(entry);

        // when
        Long result = entryService.createOne(entryVO);

        // then
        assertThat(result, equalTo(entry.getId()));
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryDAO).save(entry);
        verify(publishHandler).updatePublishDate(entry);
    }

    @Test
    public void testCreateShouldThrowConstraintViolationException() {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(DataIntegrityViolationException.class).when(entryDAO).save(entry);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> entryService.createOne(entryVO));

        // then
        // exception expected
    }

    @Test
    public void testCreateShouldThrowServiceException() {

        // given
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(IllegalArgumentException.class).when(entryDAO).save(entry);

        // when
        Assertions.assertThrows(ServiceException.class, () -> entryService.createOne(entryVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);
        given(entryDAO.updateOne(id, entry)).willReturn(Optional.of(entry));

        // when
        EntryVO result = entryService.updateOne(id, entryVO);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryToEntryVOConverter).convert(entry);
        verify(entryDAO).updateOne(id, entry);
        verify(publishHandler).updatePublishDate(id, entry);
    }

    @Test
    public void testUpdateOneWithFailure() {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        given(entryDAO.updateOne(id, entry)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.updateOne(id, entryVO));

        // then
        verify(entryVOToEntryConverter).convert(entryVO);
        verify(entryToEntryVOConverter, never()).convert(entry);
        verify(entryDAO).updateOne(id, entry);
    }

    @Test
    public void testUpdateShouldThrowConstraintViolationException() {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(DataIntegrityViolationException.class).when(entryDAO).updateOne(id, entry);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> entryService.updateOne(id, entryVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateShouldThrowServiceException() {

        // given
        Long id = 1L;
        given(entryVOToEntryConverter.convert(entryVO)).willReturn(entry);
        doThrow(IllegalArgumentException.class).when(entryDAO).updateOne(id, entry);

        // when
        Assertions.assertThrows(ServiceException.class, () -> entryService.updateOne(id, entryVO));

        // then
        // exception expected
    }

    @Test
    public void testFindByLinkWithExistingEntry() throws EntityNotFoundException {

        // given
        String link = "link-lflt-49-ut";
        given(entryDAO.findByLink(link)).willReturn(Optional.of(entry));
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        EntryVO result = entryService.findByLink(link);

        // then
        assertThat(result, equalTo(entryVO));
        verify(entryDAO).findByLink(link);
        verify(entryToEntryVOConverter).convert(entry);
    }

    @Test
    public void testFindByLinkWithNonExistingEntry() {

        // given
        String link = "link-lflt-49-ut";
        given(entryDAO.findByLink(link)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.findByLink(link));

        // then
        verify(entryDAO).findByLink(link);
        verify(entryToEntryVOConverter, never()).convert(entry);
    }

    @Test
    public void testDeleteByIdWithExistingEntry() throws ServiceException {

        // given
        given(entryDAO.exists(entryVO.getId())).willReturn(true);

        // when
        entryService.deleteByID(entryVO.getId());

        // then
        verify(entryDAO).delete(entryVO.getId());
    }

    @Test
    public void testDeleteByIdWithNonExistingEntry() {

        // given
        Long id = 1L;
        given(entryDAO.exists(entryVO.getId())).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.deleteByID(id));

        // then
        // expected exception
    }

    @Test
    public void shouldEnable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(true);

        // when
        entryService.enable(id);

        // then
        verify(entryDAO).enable(id);
    }

    @Test
    public void shouldEnableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.enable(id));

        // then
        // exception expected;
    }

    @Test
    public void shouldDisable() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(true);

        // when
        entryService.disable(id);

        // then
        verify(entryDAO).disable(id);
    }

    @Test
    public void shouldDisableThrowEntityNotFoundException() {

        // given
        Long id = 1L;
        given(entryDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.disable(id));

        // then
        // exception expected
    }

    @ParameterizedTest
    @MethodSource("publicationStatusChangeDataProvider")
    public void shouldChangePublicationStatusOfExistingEntry(EntryStatus currentStatus, EntryStatus newStatus) throws ServiceException {

        // given
        Long entryID = 1L;

        given(entryDAO.findById(entryID)).willReturn(Optional.of(entry));
        given(entry.getStatus()).willReturn(currentStatus);
        given(entryToEntryVOConverter.convert(entry)).willReturn(entryVO);

        // when
        EntryVO result = entryService.changePublicationStatus(entryID, newStatus.name());

        // then
        assertThat(result, equalTo(entryVO));

        verify(entry).setStatus(newStatus);
        verify(publishHandler).updatePublishDate(entryID, entry);
        verify(entryDAO).updateOne(entryID, entry);
    }

    @ParameterizedTest
    @MethodSource("publicationStatusChangeInvalidDataProvider")
    public void shouldChangePublicationStatusThrowExceptionOnInvalidTransition(EntryStatus currentStatus, EntryStatus newStatus) {

        // given
        Long entryID = 1L;

        given(entryDAO.findById(entryID)).willReturn(Optional.of(entry));
        given(entry.getStatus()).willReturn(currentStatus);
        given(entry.getId()).willReturn(entryID);

        // when
        Assertions.assertThrows(InvalidTransitionException.class, () -> entryService.changePublicationStatus(entryID, newStatus.name()));

        // then
        // exception expected
        verify(entry, never()).setStatus(newStatus);
        verify(entryDAO, never()).updateOne(entryID, entry);
    }

    @Test
    public void shouldChangePublicationStatusThrowExceptionOnMissingEntry() {

        // given
        Long entryID = 1L;

        given(entryDAO.findById(entryID)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> entryService.changePublicationStatus(entryID, EntryStatus.PUBLIC.name()));

        // then
        // exception expected
    }

    private static Stream<Arguments> publicationStatusChangeDataProvider() {

        return Stream.of(
                Arguments.of(EntryStatus.DRAFT, EntryStatus.REVIEW),
                Arguments.of(EntryStatus.REVIEW, EntryStatus.PUBLIC),
                Arguments.of(EntryStatus.PUBLIC, EntryStatus.DRAFT)
        );
    }

    private static Stream<Arguments> publicationStatusChangeInvalidDataProvider() {

        return Stream.of(
                Arguments.of(EntryStatus.DRAFT, EntryStatus.PUBLIC),
                Arguments.of(EntryStatus.DRAFT, EntryStatus.DRAFT),
                Arguments.of(EntryStatus.REVIEW, EntryStatus.DRAFT),
                Arguments.of(EntryStatus.REVIEW, EntryStatus.REVIEW),
                Arguments.of(EntryStatus.PUBLIC, EntryStatus.REVIEW),
                Arguments.of(EntryStatus.PUBLIC, EntryStatus.PUBLIC)
        );
    }
}
