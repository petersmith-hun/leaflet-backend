package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.attachment.AttachmentRequestModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.AttachmentBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /attachments} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class AttachmentsControllerAcceptanceTest extends AbstractTempFileHandlingParameterizedBaseTest {

    private static final Long ENTRY_ID_1 = 1L;
    private static final Long ENTRY_ID_2 = 2L;
    private static final UUID FILE_ID_1 = UUID.fromString("d4b1830d-f368-37a0-88f9-2faf7fa8ded6");
    private static final UUID FILE_ID_3 = UUID.fromString("058c9d47-6ce4-3e48-9c44-35bb9c74b378");

    @Autowired
    private AttachmentBridgeService attachmentBridgeService;

    @Autowired
    private EntryBridgeService entryBridgeService;

    @Test
    @ResetDatabase
    public void shouldAttach() throws CommunicationFailureException {

        // given
        AttachmentRequestModel attachmentRequestModel = prepareAttachmentRequestModel(ENTRY_ID_1, FILE_ID_3);

        // when
        attachmentBridgeService.attach(attachmentRequestModel);

        // then
        assertAttached(attachmentRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldKeepAttachmentIfAlreadyAttached() throws CommunicationFailureException {

        // given
        AttachmentRequestModel attachmentRequestModel = prepareAttachmentRequestModel(ENTRY_ID_2, FILE_ID_3);

        // when
        attachmentBridgeService.attach(attachmentRequestModel);

        // then
        assertAttached(attachmentRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldDetach() throws CommunicationFailureException {

        // given
        AttachmentRequestModel attachmentRequestModel = prepareAttachmentRequestModel(ENTRY_ID_1, FILE_ID_1);

        // when
        attachmentBridgeService.detach(attachmentRequestModel);

        // then
        assertDetached(attachmentRequestModel);
    }

    @Test
    @ResetDatabase
    public void shouldKeepDetachedIfNotAttached() throws CommunicationFailureException {

        // given
        AttachmentRequestModel attachmentRequestModel = prepareAttachmentRequestModel(ENTRY_ID_1, FILE_ID_3);

        // when
        attachmentBridgeService.detach(attachmentRequestModel);

        // then
        assertDetached(attachmentRequestModel);
    }

    private void assertAttached(AttachmentRequestModel attachmentRequestModel) throws CommunicationFailureException {
        assertThat(entryBridgeService.getEntryByID(attachmentRequestModel.getEntryID()).getBody()
                .getAttachments().stream()
                .anyMatch(fileDataModel -> attachmentRequestModel.getPathUUID().equals(extractUUIDFromReference(fileDataModel))), is(true));
    }

    private void assertDetached(AttachmentRequestModel attachmentRequestModel) throws CommunicationFailureException {
        assertThat(entryBridgeService.getEntryByID(attachmentRequestModel.getEntryID()).getBody()
                .getAttachments().stream()
                .noneMatch(fileDataModel -> attachmentRequestModel.getPathUUID().equals(extractUUIDFromReference(fileDataModel))), is(true));
    }

    private UUID extractUUIDFromReference(FileDataModel fileDataModel) {
        return UUID.fromString(fileDataModel.getReference().split("/")[1]);
    }

    private AttachmentRequestModel prepareAttachmentRequestModel(Long entryID, UUID fileID) {

        AttachmentRequestModel attachmentRequestModel = new AttachmentRequestModel();
        attachmentRequestModel.setEntryID(entryID);
        attachmentRequestModel.setPathUUID(fileID);

        return attachmentRequestModel;
    }
}
