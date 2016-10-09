package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.AttachmentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for {@link AttachmentServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class AttachmentServiceImplIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private static final String ATTACHMENT_1 = "attachment_1";
    private static final String ATTACHMENT_NEW = "attachment_new";

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private TestObjectReader testObjectReader;

    private AttachmentVO controlAttachmentVO;

    @Before
    public void setup() throws IOException {
        controlAttachmentVO = testObjectReader.read(ATTACHMENT_1, TestObjectReader.ObjectDirectory.VO, AttachmentVO.class);
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testGetOne() throws ServiceException {

        // when
        AttachmentVO result = attachmentService.getOne(controlAttachmentVO.getId());

        // then
        assertThat(result.getTitle(), equalTo(controlAttachmentVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testGetAll() throws ServiceException {

        // when
        List<AttachmentVO> result = attachmentService.getAll();

        // then
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.size(), equalTo(10));
        assertThat(result.get(0).getTitle(), equalTo(controlAttachmentVO.getTitle()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testCount() {

        // when
        Long result = attachmentService.count();

        // then
        assertThat(result, equalTo(10L));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testFindByEntry() {

        // given
        Long entryID = 2L;
        EntryVO entryVO = new EntryVO.Builder()
                .withId(entryID)
                .withEntryStatus("PUBLIC")
                .createEntryVO();

        // when
        List<AttachmentVO> result = attachmentService.findByEntry(entryVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(3));
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.stream().allMatch(e -> e.getEntryVO().getId().equals(entryID)), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testGetPublicAttachmentsForEntry() {

        // given
        EntryVO entryVO = new EntryVO.Builder()
                .withId(1L)
                .withEntryStatus("PUBLIC")
                .createEntryVO();

        // when
        List<AttachmentVO> result = attachmentService.getPublicAttachmentsForEntry(entryVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(4));
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.stream().allMatch(e -> e.isEnabled()), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    @Parameters(method = "pageOfAttachments", source = PagingParameterProvider.class)
    public void testGetEntityPage(int page, int itemNumber) {

        // given
        int limit = 6;
        OrderDirection direction = OrderDirection.DESC;
        AttachmentVO.OrderBy orderBy = AttachmentVO.OrderBy.CREATED;

        // when
        EntityPageVO<AttachmentVO> result = attachmentService.getEntityPage(page, limit, direction, orderBy);

        // then
        assertThat(result.getEntityCountOnPage(), equalTo(itemNumber));
        assertThat(result.getEntityCount(), equalTo(10L));
        assertThat(result.getPageCount(), equalTo(2));
        assertThat(result.getPageSize(), equalTo(limit));
        assertThat(result.getEntitiesOnPage(), notNullValue());
        assertThat(result.getEntitiesOnPage().stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.getEntitiesOnPage().size(), equalTo(itemNumber));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testCreateOne() throws ServiceException, IOException {

        // given
        AttachmentVO attachmentToCreate = testObjectReader.read(ATTACHMENT_NEW, TestObjectReader.ObjectDirectory.VO, AttachmentVO.class);

        // when
        Long result = attachmentService.createOne(attachmentToCreate);

        // then
        assertThat(result, greaterThanOrEqualTo(11L));
        assertThat(attachmentService.getOne(result), notNullValue());
        assertThat(attachmentService.getOne(result).getTitle(), equalTo("Attachment 11"));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedAttachmentTitle = "Updated attachment title";
        Long id = 2L;
        AttachmentVO attachmentToUpdate = attachmentService.getOne(id);
        attachmentToUpdate.setTitle(updatedAttachmentTitle);

        // when
        AttachmentVO result = attachmentService.updateOne(id, attachmentToUpdate);

        // then
        AttachmentVO updatedAttachmentVO = attachmentService.getOne(id);
        assertThat(updatedAttachmentVO, notNullValue());
        assertThat(updatedAttachmentVO.getTitle(), equalTo(updatedAttachmentTitle));
        assertThat(result.equals(updatedAttachmentVO), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testDeleteByEntity() throws ServiceException {

        // given
        Long id = 3L;
        AttachmentVO attachmentToDelete = attachmentService.getOne(id);

        // when
        attachmentService.deleteByEntity(attachmentToDelete);

        // then
        assertThat(attachmentService.count(), equalTo(9L));
        assertThat(attachmentService.getAll().stream().noneMatch(e -> attachmentToDelete.equals(e)), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testEnable() throws ServiceException {

        // given
        Long id = 2L;

        // when
        attachmentService.enable(id);

        // then
        assertThat(attachmentService.getOne(id).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS)
    public void testDisable() throws ServiceException {

        // given
        Long id = 1L;

        // when
        attachmentService.disable(id);

        // then
        assertThat(attachmentService.getOne(id).isEnabled(), equalTo(false));
    }

    public static class PagingParameterProvider {

        public static Object[] pageOfAttachments() {
            return new Object[] {
                    new Object[] {1, 6},
                    new Object[] {2, 4}
            };
        }
    }
}
