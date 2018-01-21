package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.service.vo.FileInputVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Utility component for generating URL-friendly filenames.
 *
 * @author Peter Smith
 */
@Component
public class FilenameGeneratorUtil {

    public String cleanFilename(FileInputVO fileInputVO) {
        return doCleanFilename(fileInputVO.getOriginalFilename());
    }

    private String doCleanFilename(String filename) {
        return StringUtils.stripAccents(filename)
                .toLowerCase()
                .trim()
                .replace(' ', '_');
    }
}
