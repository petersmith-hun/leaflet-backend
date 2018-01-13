package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.service.vo.FileInputVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FilenameGeneratorUtil}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FilenameGeneratorUtilTest {

    @InjectMocks
    private FilenameGeneratorUtil filenameGeneratorUtil;

    @Test
    public void shouldCleanFilename() {

        // given
        FileInputVO fileInputVO = FileInputVO.getBuilder()
                .withOriginalFilename(" Original Árvíztűrő Filename.jpg ")
                .build();

        // when
        String result = filenameGeneratorUtil.cleanFilename(fileInputVO);

        // then
        assertThat(result, equalTo("original_arvizturo_filename.jpg"));
    }
}