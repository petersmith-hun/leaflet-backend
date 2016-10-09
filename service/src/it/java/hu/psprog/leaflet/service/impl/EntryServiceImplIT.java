package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.EntryVO;
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

/**
 * Integration tests for {@link EntryServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class EntryServiceImplIT {

    private static final String ENTRY_1 = "entry_1";
    private static final String ENTRY_NEW = "entry_new";

    @Autowired
    private EntryService entryService;

    @Autowired
    private TestObjectReader testObjectReader;

    private EntryVO controlEntryVO;

    @Before
    public void setup() throws IOException {
        controlEntryVO = testObjectReader.read(ENTRY_1, TestObjectReader.ObjectDirectory.VO, EntryVO.class);
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testGetOne() throws ServiceException {

        // when
        EntryVO result = entryService.getOne(controlEntryVO.getId());

        // then
        assertThat(result.getTitle(), equalTo(controlEntryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testGetAll() {

        // when
        List<EntryVO> result = entryService.getAll();

        // then
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.get(0).getTitle(), equalTo(controlEntryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testCount() {

        // when
        long result = entryService.count();

        // then
        assertThat(result, equalTo(3L));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testCreateOne() throws IOException, ServiceException {

        // given
        EntryVO createdEntry = testObjectReader.read(ENTRY_NEW, TestObjectReader.ObjectDirectory.VO, EntryVO.class);

        // when
        Long result = entryService.createOne(createdEntry);

        // then
        assertThat(entryService.getOne(result).getTitle(), equalTo(createdEntry.getTitle()));
        assertThat(entryService.getOne(result).getOwner().getEmail(), equalTo("lflt-it-5101@leaflet.dev"));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedEntryTitle = "Entry updated title";
        Long id = 2L;
        EntryVO entryToUpdate = entryService.getOne(id);
        entryToUpdate.setTitle(updatedEntryTitle);

        // when
        EntryVO result = entryService.updateOne(id, entryToUpdate);

        // then
        EntryVO updatedEntryVO = entryService.getOne(id);
        assertThat(result.getTitle(), equalTo(updatedEntryVO.getTitle()));
        assertThat(result.getTitle(), equalTo(updatedEntryTitle));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testFindByLink() throws EntityNotFoundException {

        // when
        EntryVO result = entryService.findByLink(controlEntryVO.getLink());

        // then
        assertThat(result.getId(), equalTo(controlEntryVO.getId()));
        assertThat(result.getTitle(), equalTo(controlEntryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testEnable() throws ServiceException {

        // given
        Long id = 2L;

        // when
        entryService.enable(id);

        // then
        assertThat(entryService.getOne(id).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testDisable() throws ServiceException {

        // given
        Long id = 1L;

        // when
        entryService.disable(id);

        // then
        assertThat(entryService.getOne(id).isEnabled(), equalTo(false));
    }
}
