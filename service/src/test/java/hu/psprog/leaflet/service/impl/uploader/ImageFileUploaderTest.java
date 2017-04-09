package hu.psprog.leaflet.service.impl.uploader;

import hu.psprog.leaflet.service.exception.DirectoryCreationException;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ImageUploadAcceptor}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageFileUploaderTest {

    private static final String ORIGINAL_FILENAME = "test.jpg";
    private static final String ACCEPTED_MIME_TYPE = "image/jpg";
    private static final String UNACCEPTED_MIME_TYPE = "application/pdf";
    private static final String SUBFOLDER = "test";
    private static final String IMAGES_FOLDER = "images";
    private static final String ACCEPTED_AS_IMAGE = "IMAGE";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private File fileStorage;

    @InjectMocks
    private ImageUploadAcceptor imageFileUploader;

    private InputStream fileInputStream;

    @Before
    public void setup() throws IOException, DirectoryCreationException {
        prepareTemporaryStorage();
        imageFileUploader.initializeAcceptorRoot();
        fileInputStream = new FileInputStream(temporaryFolder.newFile());
    }

    @After
    public void teardown() throws IOException {
        if (Objects.nonNull(fileInputStream)) {
            fileInputStream.close();
        }
    }

    @Test
    public void shouldUploadFileUnderAcceptorRootWithAcceptableMIME() throws FileUploadException, IOException {

        // given
        FileInputVO fileInputVO = prepareValidFileInputVO(false);

        // when
        UploadedFileVO result = imageFileUploader.upload(fileInputVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getOriginalFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getStoredFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getAcceptedAs(), equalTo(ACCEPTED_AS_IMAGE));
        assertThat(result.getPath(), equalTo(Paths.get(temporaryFolder.getRoot().getAbsolutePath(), IMAGES_FOLDER).toString()));
    }

    @Test
    public void shouldUploadFileUnderSubfolderWithAcceptableMIME() throws FileUploadException, IOException {

        // given
        FileInputVO fileInputVO = prepareValidFileInputVO(true);

        // when
        UploadedFileVO result = imageFileUploader.upload(fileInputVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getOriginalFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getStoredFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getAcceptedAs(), equalTo(ACCEPTED_AS_IMAGE));
        assertThat(result.getPath(), equalTo(Paths.get(temporaryFolder.getRoot().getAbsolutePath(), IMAGES_FOLDER, SUBFOLDER).toString()));
    }

    @Test
    public void shouldNotUploadFileWithUnacceptableMIME() throws FileUploadException {

        // given
        FileInputVO fileInputVO = prepareInvalidFileInputVO();

        // when
        UploadedFileVO result = imageFileUploader.upload(fileInputVO);

        // then
        assertThat(result, nullValue());
    }

    private void prepareTemporaryStorage() throws IOException {
        File imagesFolder = temporaryFolder.newFolder(IMAGES_FOLDER);
        File subfolder = new File(imagesFolder, SUBFOLDER);
        if (!subfolder.mkdir()) {
            throw new IOException("Failed to prepare temporary storage");
        }
        given(fileStorage.getAbsolutePath()).willReturn(temporaryFolder.getRoot().getAbsolutePath());
    }

    private FileInputVO prepareValidFileInputVO(boolean withSubfolder) throws IOException {
        return FileInputVO.Builder.getBuilder()
                .withContentType(ACCEPTED_MIME_TYPE)
                .withOriginalFilename(ORIGINAL_FILENAME)
                .withFileContentStream(fileInputStream)
                .withRelativePath(withSubfolder ? SUBFOLDER : null)
                .build();
    }

    private FileInputVO prepareInvalidFileInputVO() {
        return FileInputVO.Builder.getBuilder()
                .withContentType(UNACCEPTED_MIME_TYPE)
                .build();
    }
}
