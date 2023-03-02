package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.testdata.TestObjects;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link AttachmentServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class AttachmentServiceImplIT {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EntryService entryService;

    private EntryVO controlEntry;
    private UploadedFile alreadyAttachedFile;
    private UploadedFile fileToAttach;

    @BeforeEach
    public void setup() throws IOException {
        controlEntry = TestObjects.ENTRY_VO_1;
        alreadyAttachedFile = TestObjects.UPLOADED_FILE_ALREADY_ATTACHED;
        fileToAttach = TestObjects.UPLOADED_FILE_TO_ATTACH;
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS})
    public void shouldAttachFileToEntry() throws EntityNotFoundException {

        // when
        attachmentService.attachFileToEntry(fileToAttach, controlEntry);

        // then
        EntryVO result = entryService.findByLink(controlEntry.getLink());
        assertThat(result.getAttachments().isEmpty(), is(false));
        assertThat(result.getAttachments().size(), equalTo(2));
        assertThat(result.getAttachments()
                .stream()
                .map(UploadedFileVO::getPath)
                .collect(Collectors.toList()), hasItems(alreadyAttachedFile.getPath(), fileToAttach.getPath()));
    }

    @Test
    @Transactional
    @Sql({LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES, LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_ATTACHMENTS})
    public void shouldDetachFileFromEntry() throws EntityNotFoundException {

        // when
        attachmentService.detachFileFromEntry(alreadyAttachedFile, controlEntry);

        // then
        EntryVO result = entryService.findByLink(controlEntry.getLink());
        assertThat(result.getAttachments().isEmpty(), is(true));
    }
}
