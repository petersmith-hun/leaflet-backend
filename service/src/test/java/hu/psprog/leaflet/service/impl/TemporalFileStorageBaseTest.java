package hu.psprog.leaflet.service.impl;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.BDDMockito.given;

/**
 * Test base for tests requiring file storage.
 *
 * @author Peter Smith
 */
public class TemporalFileStorageBaseTest {

    protected static final String SUBFOLDER = "test";
    protected static final String IMAGES_FOLDER = "images";
    protected static final String FILENAME = "test_file.jpg";
    protected static final String TEST_DATA = "testData";

    private File imagesRootFolder;
    private File imagesSubFolder;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock(lenient = true)
    protected File fileStorage;

    protected void prepareTemporaryStorage() throws IOException {
        prepareTemporaryStorage(true);
    }

    protected void prepareTemporaryStorage(boolean prepareFolders) throws IOException {
        if (prepareFolders) {
            imagesRootFolder = temporaryFolder.newFolder(IMAGES_FOLDER);
            imagesSubFolder = new File(imagesRootFolder, SUBFOLDER);
            if (!imagesSubFolder.mkdir()) {
                throw new IOException("Failed to prepare temporary storage");
            }
        }
        given(fileStorage.getAbsolutePath()).willReturn(temporaryFolder.getRoot().getAbsolutePath());
    }

    protected File prepareFileForDownload() throws IOException {

        File file = new File(getImagesRootFolder(), FILENAME);
        if (!file.createNewFile()) {
            throw new IOException("Test file could not be created");
        }
        Files.write(file.toPath(), TEST_DATA.getBytes());

        return file;
    }

    protected File getImagesRootFolder() {
        return imagesRootFolder;
    }

    protected File getImagesSubFolder() {
        return imagesSubFolder;
    }
}
