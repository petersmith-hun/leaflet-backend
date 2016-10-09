package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
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
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration test for {@link DocumentServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class DocumentServiceImplIT {

    private static final String DOCUMENT_1 = "document_1";
    private static final String DOCUMENT_NEW = "document_new";

    @Autowired
    private DocumentService documentService;

    @Autowired
    private TestObjectReader testObjectReader;

    private DocumentVO controlDocumentVO;

    @Before
    public void setup() throws IOException {
        controlDocumentVO = testObjectReader.read(DOCUMENT_1, TestObjectReader.ObjectDirectory.VO, DocumentVO.class);
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetOne() throws ServiceException {

        // when
        DocumentVO result = documentService.getOne(controlDocumentVO.getId());

        // then
        assertThat(result.getContent(), equalTo(controlDocumentVO.getContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetAll() throws ServiceException {

        // when
        List<DocumentVO> result = documentService.getAll();

        // then
        assertThat(result.stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0).getContent(), equalTo(controlDocumentVO.getContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetPageOfPublicDocuments() {

        // given
        int limit = 3;
        int page = 1;
        OrderDirection direction = OrderDirection.DESC;
        DocumentVO.OrderBy orderBy = DocumentVO.OrderBy.CREATED;

        // when
        EntityPageVO<DocumentVO> result = documentService.getPageOfPublicDocuments(page, limit, direction, orderBy);

        // then
        assertThat(result.getEntityCountOnPage(), equalTo(3));
        assertThat(result.getEntityCount(), equalTo(3L));
        assertThat(result.getPageCount(), equalTo(1));
        assertThat(result.getPageSize(), equalTo(limit));
        assertThat(result.getEntitiesOnPage(), notNullValue());
        assertThat(result.getEntitiesOnPage().stream().allMatch(e -> e != null), equalTo(true));
        assertThat(result.getEntitiesOnPage().size(), equalTo(3));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetByLink() throws ServiceException {

        // given
        String link = "duis-commodo-iaculis-20160818";

        // when
        DocumentVO result = documentService.getByLink(link);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(link));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testCount() {

        // when
        Long result = documentService.count();

        // then
        assertThat(result, equalTo(4L));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testCreateOne() throws IOException, ServiceException {

        // given
        DocumentVO documentToCreate = testObjectReader.read(DOCUMENT_NEW, TestObjectReader.ObjectDirectory.VO, DocumentVO.class);

        // when
        Long result = documentService.createOne(documentToCreate);

        // then
        assertThat(result, greaterThanOrEqualTo(5L));
        assertThat(documentService.getOne(result), notNullValue());
        assertThat(documentService.getOne(result).getTitle(), equalTo("Test document newly created"));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedDocumentTitle = "Updated document title";
        Long id = 1L;
        DocumentVO documentToUpdate = documentService.getOne(id);
        documentToUpdate.setTitle(updatedDocumentTitle);

        // when
        DocumentVO result = documentService.updateOne(id, documentToUpdate);

        // then
        DocumentVO updatedDocumentVO = documentService.getOne(id);
        assertThat(updatedDocumentVO, notNullValue());
        assertThat(updatedDocumentVO.getTitle(), equalTo(updatedDocumentTitle));
        assertThat(result.equals(updatedDocumentVO), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testDeleteByEntity() throws ServiceException {

        // given
        Long id = 3L;
        DocumentVO documentToDelete = documentService.getOne(id);

        // when
        documentService.deleteByEntity(documentToDelete);

        // then
        assertThat(documentService.count(), equalTo(3L));
        assertThat(documentService.getAll().stream().noneMatch(e -> documentToDelete.equals(e)), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testEnable() throws ServiceException {

        // given
        Long id = 3L;

        // when
        documentService.enable(id);

        // then
        assertThat(documentService.getOne(id).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testDisable() throws ServiceException {

        // given
        Long id = 2L;

        // when
        documentService.disable(id);

        // then
        assertThat(documentService.getOne(id).isEnabled(), equalTo(false));
    }
}
