package hu.psprog.leaflet.service.impl.uploader.acceptor;

import hu.psprog.leaflet.service.vo.FileInputVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for {@link ImageUploadAcceptor}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
public class ImageUploadAcceptorTest {

    private static final String ACCEPTABLE_MIME_IMAGE_JPG = "image/jpg";
    private static final String ACCEPTABLE_MIME_IMAGE_JPEG = "image/jpeg";
    private static final String ACCEPTABLE_MIME_IMAGE_PNG = "image/png";
    private static final String ACCEPTABLE_MIME_IMAGE_GIF = "image/gif";
    private static final String UNACCEPTABLE_MIME_APPLICATION_PDF = "application/pdf";

    @InjectMocks
    private ImageUploadAcceptor imageUploadAcceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(source = MIMETypeProvider.class)
    public void testAcceptance(String mime, boolean expectedResult) {

        // given
        FileInputVO fileInputVO = prepareFileInputVO(mime);

        // when
        boolean result = imageUploadAcceptor.accept(fileInputVO);

        // then
        assertThat(result, is(expectedResult));
    }

    private FileInputVO prepareFileInputVO(String mime) {
        return FileInputVO.Builder.getBuilder()
                .withContentType(mime)
                .build();
    }

    public static class MIMETypeProvider {

        public static Object[] provide() {
            return new Object[] {
                    new Object[] {ACCEPTABLE_MIME_IMAGE_GIF, true},
                    new Object[] {ACCEPTABLE_MIME_IMAGE_JPEG, true},
                    new Object[] {ACCEPTABLE_MIME_IMAGE_JPG, true},
                    new Object[] {ACCEPTABLE_MIME_IMAGE_PNG, true},
                    new Object[] {UNACCEPTABLE_MIME_APPLICATION_PDF, false}
            };
        }
    }
}
