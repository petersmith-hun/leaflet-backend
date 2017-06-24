package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.TagService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class TagServiceImplIT {

    private static final String TAG_1 = "tag_1";
    private static final String TAG_NEW = "tag_new";
    private static final String ENTRY_1 = "entry_1";
    private static final String TAG_TO_ATTACH = "tag_to_attach";
    private static final String ALREADY_ATTACHED_TAG = "tag_already_attached";

    @Autowired
    private TagService tagService;

    @Autowired
    private EntryService entryService;

    @Autowired
    private TestObjectReader testObjectReader;

    private TagVO controlTagVO;
    private TagVO alreadyAttachedTag;
    private TagVO tagToAttach;
    private EntryVO controlEntryVO;

    @Before
    public void setup() throws IOException {
        controlTagVO = testObjectReader.read(TAG_1, TestObjectReader.ObjectDirectory.VO, TagVO.class);
        controlEntryVO = testObjectReader.read(ENTRY_1, TestObjectReader.ObjectDirectory.VO, EntryVO.class);
        alreadyAttachedTag = testObjectReader.read(ALREADY_ATTACHED_TAG, TestObjectReader.ObjectDirectory.VO, TagVO.class);
        tagToAttach = testObjectReader.read(TAG_TO_ATTACH, TestObjectReader.ObjectDirectory.VO, TagVO.class);
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
    public void testGetAll() throws ServiceException {

        // when
        List<TagVO> result = tagService.getAll();

        // then
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
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
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.stream().allMatch(e -> e.isEnabled()), equalTo(true));
        assertThat(result.size(), equalTo(14));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testCount() {

        // when
        Long result = tagService.count();

        // then
        assertThat(result, equalTo(20L));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_TAGS})
    public void testCreateOne() throws ServiceException, IOException {

        // given
        TagVO tagToCreate = testObjectReader.read(TAG_NEW, TestObjectReader.ObjectDirectory.VO, TagVO.class);

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
        tagService.deleteByEntity(tagToDelete);

        // then
        assertThat(tagService.count(), equalTo(19L));
        assertThat(tagService.getAll().stream().noneMatch(e -> tagToDelete.equals(e)), equalTo(true));
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
