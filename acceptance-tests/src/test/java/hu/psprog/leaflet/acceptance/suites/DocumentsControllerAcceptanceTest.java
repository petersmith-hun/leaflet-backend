package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /documents} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class DocumentsControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_DOCUMENTS = 4;
    private static final int NUMBER_OF_PUBLIC_DOCUMENTS = 3;

    private static final String CONTROL_DOCUMENT_LINK = "doc-1";
    private static final String CONTROL_DOCUMENT_5 = "doc-5";
    private static final Long CONTROL_DOCUMENT_ID = 1L;
    private static final GenericType<WrapperBodyDataModel<DocumentDataModel>> GENERIC_TYPE_DOCUMENT_DATA_MODEL = new GenericType<WrapperBodyDataModel<DocumentDataModel>>() {};
    private static final GenericType<WrapperBodyDataModel<EditDocumentDataModel>> GENERIC_TYPE_EDIT_DOCUMENT_DATA_MODEL = new GenericType<WrapperBodyDataModel<EditDocumentDataModel>>() {};

    @Autowired
    private DocumentBridgeService documentBridgeService;

    @Test
    public void shouldReturnAllDocuments() throws CommunicationFailureException {

        // when
        DocumentListDataModel result = documentBridgeService.getAllDocuments();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getDocuments().size(), equalTo(NUMBER_OF_ALL_DOCUMENTS));
    }

    @Test
    public void shouldReturnPublicDocuments() throws CommunicationFailureException {

        // when
        DocumentListDataModel result = documentBridgeService.getPublicDocuments();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getDocuments().size(), equalTo(NUMBER_OF_PUBLIC_DOCUMENTS));
        assertThat(result.getDocuments().stream().allMatch(EditDocumentDataModel::isEnabled), is(true));
    }

    @Test
    public void shouldReturnDocumentByLink() throws CommunicationFailureException {

        // given
        WrapperBodyDataModel<DocumentDataModel> control = getControl(CONTROL_DOCUMENT_LINK, GENERIC_TYPE_DOCUMENT_DATA_MODEL);

        // when
        WrapperBodyDataModel<DocumentDataModel> result = documentBridgeService.getDocumentByLink(CONTROL_DOCUMENT_LINK);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDocumentByLinkReturnHTTP404ForNonExistingDocument() throws CommunicationFailureException {

        // given
        String nonExistingDocument = "doc-non-existing";

        // when
        documentBridgeService.getDocumentByLink(nonExistingDocument);

        // then
        // exception expected
    }

    @Test
    public void shouldReturnDocumentByID() throws CommunicationFailureException {

        // given
        WrapperBodyDataModel<EditDocumentDataModel> control = getControl(CONTROL_DOCUMENT_LINK, CONTROL_SUFFIX_EDIT, GENERIC_TYPE_EDIT_DOCUMENT_DATA_MODEL);

        // when
        WrapperBodyDataModel<EditDocumentDataModel> result = documentBridgeService.getDocumentByID(CONTROL_DOCUMENT_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    @ResetDatabase
    public void shouldChangeStatus() throws CommunicationFailureException {

        // when
        documentBridgeService.changeStatus(CONTROL_DOCUMENT_ID);

        // then
        DocumentListDataModel current = documentBridgeService.getPublicDocuments();
        assertThat(current.getDocuments().stream().noneMatch(document -> CONTROL_DOCUMENT_LINK.equals(document.getLink())), is(true));
        assertThat(current.getDocuments().size(), equalTo(2));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteDocument() throws CommunicationFailureException {

        // when
        documentBridgeService.deleteDocument(CONTROL_DOCUMENT_ID);

        // then
        DocumentListDataModel current = documentBridgeService.getAllDocuments();
        assertThat(current.getDocuments().stream().noneMatch(document -> CONTROL_DOCUMENT_LINK.equals(document.getLink())), is(true));
        assertThat(current.getDocuments().size(), equalTo(3));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateDocument() throws CommunicationFailureException {

        // given
        DocumentUpdateRequestModel documentUpdateRequestModel = getControl(CONTROL_DOCUMENT_LINK, CONTROL_SUFFIX_MODIFY, DocumentUpdateRequestModel.class);

        // when
        documentBridgeService.updateDocument(CONTROL_DOCUMENT_ID, documentUpdateRequestModel);

        // then
        assertModifiedDocuments(CONTROL_DOCUMENT_ID, documentUpdateRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldCreateDocument() throws CommunicationFailureException {

        // given
        DocumentCreateRequestModel documentCreateRequestModel = getControl(CONTROL_DOCUMENT_5, CONTROL_SUFFIX_CREATE, DocumentCreateRequestModel.class);

        // when
        EditDocumentDataModel result = documentBridgeService.createDocument(documentCreateRequestModel);

        // then
        assertModifiedDocuments(result.getId(), documentCreateRequestModel);
    }

    private void assertModifiedDocuments(Long documentID, DocumentUpdateRequestModel expected) throws CommunicationFailureException {
        WrapperBodyDataModel<EditDocumentDataModel> current = documentBridgeService.getDocumentByID(documentID);
        assertThat(current.getBody().getRawContent(), equalTo(expected.getRawContent()));
        assertThat(current.getBody().getTitle(), equalTo(expected.getTitle()));
        assertThat(current.getBody().getLink(), equalTo(expected.getLink()));
        assertThat(current.getBody().getLocale().toLowerCase(), equalTo(expected.getLocale().getLanguage()));
        assertThat(current.getBody().isEnabled(), equalTo(expected.isEnabled()));
        assertThat(current.getSeo().getMetaTitle(), equalTo(expected.getMetaTitle()));
        assertThat(current.getSeo().getMetaDescription(), equalTo(expected.getMetaDescription()));
        assertThat(current.getSeo().getMetaKeywords(), equalTo(expected.getMetaKeywords()));

        if (expected instanceof DocumentCreateRequestModel) {
            assertThat(current.getBody().getUser().getId(), equalTo(((DocumentCreateRequestModel) expected).getUserID()));
        }
    }
}
