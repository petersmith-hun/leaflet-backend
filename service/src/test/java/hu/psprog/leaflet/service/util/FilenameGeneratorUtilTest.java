package hu.psprog.leaflet.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for {@link FilenameGeneratorUtil}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class FilenameGeneratorUtilTest {

    @InjectMocks
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @Test
    public void shouldCleanFilename() {

        // given
        String originalFilename = " Original Árvíztűrő Filename.jpg ";

        // when
        String result = filenameGeneratorUtil.doCleanFilename(originalFilename);

        // then
        assertThat(result, equalTo("original_arvizturo_filename.jpg"));
    }
}