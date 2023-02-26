package hu.psprog.leaflet.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Utility component for generating URL-friendly filenames.
 *
 * @author Peter Smith
 */
@Component
public class FilenameGeneratorUtil {

    public String doCleanFilename(String filename) {
        return StringUtils.stripAccents(filename)
                .toLowerCase()
                .trim()
                .replace(' ', '_');
    }
}
