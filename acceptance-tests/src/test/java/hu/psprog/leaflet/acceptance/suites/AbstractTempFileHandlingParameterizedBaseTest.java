package hu.psprog.leaflet.acceptance.suites;

import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Creates temporal files for tests.
 *
 * @author Peter Smith
 */
public abstract class AbstractTempFileHandlingParameterizedBaseTest extends AbstractParameterizedBaseTest {

    private static final String IMAGES_DIRECTORY = "images";
    private static final String TEST_SUB_DIRECTORY = "test_sub";
    private static final String STORED_FILENAME_1 = "stored_filename_1.jpg";
    private static final String STORED_FILENAME_2 = "stored_filename_2.jpg";
    private static final String STORED_FILENAME_3 = "stored_filename_3.jpg";

    private static File tempFileForFile1;
    private static File tempFileForFile2;
    private static File tempFileForFile3;
    private static File tempDirectoryForFile3;
    private static File tempImagesRoot;

    @Value("${java.io.tmpdir}")
    private String baseDirectory;

    @PostConstruct
    public void setupClass() throws IOException {

        tempImagesRoot = new File(baseDirectory, IMAGES_DIRECTORY);
        tempDirectoryForFile3 = new File(tempImagesRoot, TEST_SUB_DIRECTORY);
        tempFileForFile1 = new File(tempImagesRoot, STORED_FILENAME_1);
        tempFileForFile2 = new File(tempImagesRoot, STORED_FILENAME_2);
        tempFileForFile3 = new File(tempDirectoryForFile3.getPath(), STORED_FILENAME_3);

        if (!(tempImagesRoot.exists() || tempImagesRoot.mkdir())) {
            fail("Could not create images acceptor root directory");
        }

        if (!(tempDirectoryForFile3.exists() || tempDirectoryForFile3.mkdir())) {
            fail("Could not create test_sub directory.");
        }
        if (!(tempFileForFile1.exists() || tempFileForFile1.createNewFile())) {
            fail("Could not create file #1");
        }
        if (!(tempFileForFile2.exists() || tempFileForFile2.createNewFile())) {
            fail("Could not create file #2");
        }
        if (!(tempFileForFile3.exists() || tempFileForFile3.createNewFile())) {
            fail("Could not create file #3");
        }
    }

    @AfterAll
    public static void tearDownClass() throws IOException {

        // remove files
        Files.walk(tempImagesRoot.toPath()).forEach(path -> {
            File currentFile = path.toFile();
            if (!currentFile.isDirectory()) {
                currentFile.delete();
            }
        });

        // remove directories
        Files.walk(tempImagesRoot.toPath()).forEach(path -> path.toFile().delete());

        if (!tempImagesRoot.delete()) {
            fail("Could not remove all test files.");
        }
    }
}
