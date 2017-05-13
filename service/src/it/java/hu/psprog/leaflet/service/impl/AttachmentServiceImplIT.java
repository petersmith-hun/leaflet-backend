package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.AttachmentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link AttachmentServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class AttachmentServiceImplIT {

    private static final String ENTRY_1 = "entry_1";
    private static final String ALREADY_ATTACHED_FILE = "uploaded_file_already_attached";
    private static final String FILE_TO_ATTACH = "uploaded_file_to_attach";

    @Autowired
    private TestObjectReader testObjectReader;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EntryService entryService;

    private EntryVO controlEntry;
    private UploadedFileVO alreadyAttachedFile;
    private UploadedFileVO fileToAttach;

    @Before
    public void setup() throws IOException {
        controlEntry = testObjectReader.read(ENTRY_1, TestObjectReader.ObjectDirectory.VO, EntryVO.class);
        alreadyAttachedFile = testObjectReader.read(ALREADY_ATTACHED_FILE, TestObjectReader.ObjectDirectory.VO, UploadedFileVO.class);
        fileToAttach = testObjectReader.read(FILE_TO_ATTACH, TestObjectReader.ObjectDirectory.VO, UploadedFileVO.class);
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
        assertThat(result.getAttachments().contains(alreadyAttachedFile), is(true));
        assertThat(result.getAttachments().contains(fileToAttach), is(true));
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
