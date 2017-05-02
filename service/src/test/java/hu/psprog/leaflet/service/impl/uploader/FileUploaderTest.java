package hu.psprog.leaflet.service.impl.uploader;

import hu.psprog.leaflet.service.exception.DirectoryCreationException;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.impl.TemporalFileStorageBaseTest;
import hu.psprog.leaflet.service.impl.uploader.acceptor.ImageUploadAcceptor;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

/**
 * Unit tests for {@link ImageUploadAcceptor}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FileUploaderTest extends TemporalFileStorageBaseTest {

    private static final String ORIGINAL_FILENAME = "test.jpg";
    private static final String DESTINATION_FILENAME = "destination_filename.jpg";
    private static final String MIME_TYPE = "image/jpg";
    private static final String ACCEPTED_AS_IMAGE = "IMAGE";
    private static final UUID PATH_UUID_NO_SUBFOLDER = UUID.fromString("b69aeb6f-cd6b-34e4-be52-ed0d825d3a72");
    private static final UUID PATH_UUID_SUBFOLDER = UUID.fromString("2075f10c-e190-3cee-a99c-63ebafb5bc6e");

    @Mock
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @Mock
    private ImageUploadAcceptor imageUploadAcceptor;

    private FileUploader fileUploader;
    private InputStream fileInputStream;

    @Before
    public void setup() throws IOException, DirectoryCreationException {
        prepareTemporaryStorage();
        given(imageUploadAcceptor.acceptedAs()).willReturn(ACCEPTED_AS_IMAGE);
        given(imageUploadAcceptor.groupRootDirectory()).willReturn(IMAGES_FOLDER);
        given(filenameGeneratorUtil.cleanFilename(any(FileInputVO.class))).willReturn(DESTINATION_FILENAME);
        fileUploader = new FileUploader(fileStorage, Arrays.asList(imageUploadAcceptor), filenameGeneratorUtil);
        fileInputStream = new FileInputStream(prepareTempFile());
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
        given(imageUploadAcceptor.accept(fileInputVO)).willReturn(true);

        // when
        UploadedFileVO result = fileUploader.upload(fileInputVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getOriginalFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getAcceptedAs(), equalTo(ACCEPTED_AS_IMAGE));
        assertThat(result.getPath(), equalTo(Paths.get(IMAGES_FOLDER, DESTINATION_FILENAME).toString()));
        assertThat(result.getPathUUID(), equalTo(PATH_UUID_NO_SUBFOLDER));
    }

    @Test
    public void shouldUploadFileUnderSubfolderWithAcceptableMIME() throws FileUploadException, IOException {

        // given
        FileInputVO fileInputVO = prepareValidFileInputVO(true);
        given(imageUploadAcceptor.accept(fileInputVO)).willReturn(true);

        // when
        UploadedFileVO result = fileUploader.upload(fileInputVO);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getOriginalFilename(), equalTo(ORIGINAL_FILENAME));
        assertThat(result.getAcceptedAs(), equalTo(ACCEPTED_AS_IMAGE));
        assertThat(result.getPath(), equalTo(Paths.get(IMAGES_FOLDER, SUBFOLDER, DESTINATION_FILENAME).toString()));
        assertThat(result.getPathUUID(), equalTo(PATH_UUID_SUBFOLDER));
    }

    @Test
    public void shouldNotUploadFileWithUnacceptableMIME() throws FileUploadException, IOException {

        // given
        FileInputVO fileInputVO = prepareValidFileInputVO(false);
        given(imageUploadAcceptor.accept(fileInputVO)).willReturn(false);

        // when
        UploadedFileVO result = fileUploader.upload(fileInputVO);

        // then
        assertThat(result, nullValue());
    }

    private FileInputVO prepareValidFileInputVO(boolean withSubfolder) throws IOException {
        return FileInputVO.Builder.getBuilder()
                .withContentType(MIME_TYPE)
                .withOriginalFilename(ORIGINAL_FILENAME)
                .withFileContentStream(fileInputStream)
                .withRelativePath(withSubfolder ? SUBFOLDER : null)
                .build();
    }

    private File prepareTempFile() throws IOException {

        File tempFile = temporaryFolder.newFile();
        List<String> lines = Arrays.asList("Line #1", "Line #2", "Line #3");
        Files.write(tempFile.toPath(), lines);

        return tempFile;
    }
}
