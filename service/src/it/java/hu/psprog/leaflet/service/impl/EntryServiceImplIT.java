package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.testdata.TestObjects;
import hu.psprog.leaflet.service.vo.EntryVO;
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

/**
 * Integration tests for {@link EntryServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class EntryServiceImplIT {

    @Autowired
    private EntryService entryService;

    private EntryVO controlEntryVO;

    @BeforeEach
    public void setup() throws IOException {
        controlEntryVO = TestObjects.ENTRY_VO_1;
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
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.get(0).getTitle(), equalTo(controlEntryVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void testCreateOne() throws ServiceException {

        // given
        EntryVO createdEntry = TestObjects.ENTRY_VO_NEW;

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
        EntryVO updateVO = EntryVO.getBuilder()
                .withId(id)
                .withTitle(updatedEntryTitle)
                .withEntryStatus(entryToUpdate.getEntryStatus())
                .withRawContent(entryToUpdate.getRawContent())
                .build();

        // when
        EntryVO result = entryService.updateOne(id, updateVO);

        // then
        EntryVO updatedEntryVO = entryService.getOne(id);
        assertThat(result.getTitle(), equalTo(updatedEntryVO.getTitle()));
        assertThat(result.getTitle(), equalTo(updatedEntryTitle));
        assertThat(updatedEntryVO.getRawContent(), equalTo(entryToUpdate.getRawContent()));
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
