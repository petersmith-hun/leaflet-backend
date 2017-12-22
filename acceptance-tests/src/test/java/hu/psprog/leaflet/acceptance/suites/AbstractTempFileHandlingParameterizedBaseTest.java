package hu.psprog.leaflet.acceptance.suites;

import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;

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

    @Value("${java.io.tmpdir}")
    private String baseDirectory;

    @PostConstruct
    public void setupClass() throws IOException {

        File imagesRoot = new File(baseDirectory, IMAGES_DIRECTORY);
        tempDirectoryForFile3 = new File(imagesRoot, TEST_SUB_DIRECTORY);
        tempFileForFile1 = new File(imagesRoot, STORED_FILENAME_1);
        tempFileForFile2 = new File(imagesRoot, STORED_FILENAME_2);
        tempFileForFile3 = new File(tempDirectoryForFile3.getPath(), STORED_FILENAME_3);

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

    @AfterClass
    public static void tearDownClass() {

        boolean clean;
        clean = tempFileForFile1.delete();
        clean &= tempFileForFile2.delete();
        clean &= tempFileForFile3.delete();
        clean &= tempDirectoryForFile3.delete();

        if (!clean) {
            fail("Could not remove all test files.");
        }
    }
}
