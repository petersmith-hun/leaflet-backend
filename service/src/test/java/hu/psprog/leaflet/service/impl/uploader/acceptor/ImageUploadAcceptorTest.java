package hu.psprog.leaflet.service.impl.uploader.acceptor;

import hu.psprog.leaflet.service.vo.FileInputVO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for {@link ImageUploadAcceptor}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ImageUploadAcceptorTest {

    private static final String ACCEPTABLE_MIME_IMAGE_JPG = "image/jpg";
    private static final String ACCEPTABLE_MIME_IMAGE_JPEG = "image/jpeg";
    private static final String ACCEPTABLE_MIME_IMAGE_PNG = "image/png";
    private static final String ACCEPTABLE_MIME_IMAGE_GIF = "image/gif";
    private static final String UNACCEPTABLE_MIME_APPLICATION_PDF = "application/pdf";

    @InjectMocks
    private ImageUploadAcceptor imageUploadAcceptor;

    @ParameterizedTest
    @MethodSource("provideDataSource")
    public void testAcceptance(String mime, boolean expectedResult) {

        // given
        FileInputVO fileInputVO = prepareFileInputVO(mime);

        // when
        boolean result = imageUploadAcceptor.accept(fileInputVO);

        // then
        assertThat(result, is(expectedResult));
    }

    private FileInputVO prepareFileInputVO(String mime) {
        return FileInputVO.getBuilder()
                .withContentType(mime)
                .build();
    }

    public static Stream<Arguments> provideDataSource() {
        
        return Stream.of(
                Arguments.of(ACCEPTABLE_MIME_IMAGE_GIF, true),
                Arguments.of(ACCEPTABLE_MIME_IMAGE_JPEG, true),
                Arguments.of(ACCEPTABLE_MIME_IMAGE_JPG, true),
                Arguments.of(ACCEPTABLE_MIME_IMAGE_PNG, true),
                Arguments.of(UNACCEPTABLE_MIME_APPLICATION_PDF, false)
        );
    }
}
