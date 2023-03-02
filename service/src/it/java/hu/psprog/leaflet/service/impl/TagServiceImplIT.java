package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.testdata.TestObjects;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.SelfStatusAwareIdentifiableVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for {@link TagServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class TagServiceImplIT {

    @Autowired
    private TagService tagService;

    @Autowired
    private EntryService entryService;

    private TagVO controlTagVO;
    private TagVO alreadyAttachedTag;
    private TagVO tagToAttach;
    private EntryVO controlEntryVO;

    @BeforeEach
    public void setup() throws IOException {
        controlTagVO = TestObjects.TAG_VO_1;
        controlEntryVO = TestObjects.ENTRY_VO_1;
        alreadyAttachedTag = TestObjects.TAG_VO_ALREADY_ATTACHED;
        tagToAttach = TestObjects.TAG_VO_TO_ATTACH;
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testGetOne() throws ServiceException {

        // when
        TagVO result = tagService.getOne(controlTagVO.getId());

        // then
        assertThat(result.getTitle(), equalTo(controlTagVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testGetAll() {

        // when
        List<TagVO> result = tagService.getAll();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.size(), equalTo(20));
        assertThat(result.get(0).getTitle(), equalTo(controlTagVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testGetPublicTags() {

        // when
        List<TagVO> result = tagService.getPublicTags();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.stream().allMatch(SelfStatusAwareIdentifiableVO::isEnabled), equalTo(true));
        assertThat(result.size(), equalTo(14));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testCreateOne() throws ServiceException {

        // given
        TagVO tagToCreate = TestObjects.TAG_VO_NEW;

        // when
        Long result = tagService.createOne(tagToCreate);

        // then
        assertThat(result, greaterThanOrEqualTo(21L));
        assertThat(tagService.getOne(result), notNullValue());
        assertThat(tagService.getOne(result).getTitle(), equalTo("Tag #21"));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedTagTitle = "Updated tag title";
        Long id = 1L;
        TagVO updateVO = TagVO.getBuilder()
                .withId(id)
                .withTitle(updatedTagTitle)
                .build();

        // when
        TagVO result = tagService.updateOne(id, updateVO);

        // then
        TagVO updatedTagVO = tagService.getOne(id);
        assertThat(updatedTagVO, notNullValue());
        assertThat(updatedTagVO.getTitle(), equalTo(updatedTagTitle));
        assertThat(result.equals(updatedTagVO), equalTo(true));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testDeleteByEntity() throws ServiceException {

        // given
        Long id = 2L;
        TagVO tagToDelete = tagService.getOne(id);

        // when
        tagService.deleteByID(id);

        // then
        List<TagVO> allTags = tagService.getAll();
        assertThat(allTags.size(), equalTo(19));
        assertThat(allTags.stream().noneMatch(tagToDelete::equals), equalTo(true));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testEnable() throws ServiceException {

        // given
        Long id = 6L;

        // when
        tagService.enable(id);

        // then
        assertThat(tagService.getOne(id).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testDisable() throws ServiceException {

        // given
        Long id = 7L;

        // when
        tagService.disable(id);

        // then
        assertThat(tagService.getOne(id).isEnabled(), equalTo(false));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void shouldAttachTagToEntry() throws ServiceException {

        // when
        tagService.attachTagToEntry(tagToAttach, controlEntryVO);

        // then
        EntryVO result = entryService.findByLink(controlEntryVO.getLink());
        assertThat(result.getTags().isEmpty(), is(false));
        assertThat(result.getTags().size(), equalTo(2));
        assertThat(result.getTags().contains(alreadyAttachedTag), is(true));
        assertThat(result.getTags().contains(tagToAttach), is(true));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void shouldDetachTagFromEntry() throws ServiceException {

        // when
        tagService.detachTagFromEntry(alreadyAttachedTag, controlEntryVO);

        // then
        EntryVO result = entryService.findByLink(controlEntryVO.getLink());
        assertThat(result.getTags().isEmpty(), is(true));
    }
}
