package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.testdata.TestObjects;
import hu.psprog.leaflet.service.vo.DocumentVO;
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
 * Integration test for {@link DocumentServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class DocumentServiceImplIT {

    @Autowired
    private DocumentService documentService;

    private DocumentVO controlDocumentVO;

    @BeforeEach
    public void setup() throws IOException {
        controlDocumentVO = TestObjects.DOCUMENT_VO_1;
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetOne() throws ServiceException {

        // when
        DocumentVO result = documentService.getOne(controlDocumentVO.getId());

        // then
        assertThat(result.getRawContent(), equalTo(controlDocumentVO.getRawContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetAll() {

        // when
        List<DocumentVO> result = documentService.getAll();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0).getRawContent(), equalTo(controlDocumentVO.getRawContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void testGetPublicDocuments() {

        // when
        List<DocumentVO> result = documentService.getPublicDocuments();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(3));
        assertThat(result.stream().allMatch(DocumentVO::isEnabled), is(true));
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
    public void testCreateOne() throws ServiceException {

        // given
        DocumentVO documentToCreate = TestObjects.DOCUMENT_VO_NEW;

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
        DocumentVO updateVO = DocumentVO.getBuilder()
                .withId(id)
                .withTitle(updatedDocumentTitle)
                .build();

        // when
        DocumentVO result = documentService.updateOne(id, updateVO);

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
        documentService.deleteByID(id);

        // then
        List<DocumentVO> allDocuments = documentService.getAll();
        assertThat(allDocuments.size(), equalTo(3));
        assertThat(allDocuments.stream().noneMatch(documentToDelete::equals), equalTo(true));
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
