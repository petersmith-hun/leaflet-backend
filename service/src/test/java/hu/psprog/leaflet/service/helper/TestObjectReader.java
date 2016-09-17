package hu.psprog.leaflet.service.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Helper class for reading test objects in JSON format.
 *
 * @author Peter Smith
 */
public class TestObjectReader {

    private static final String TEST_OBJECT_BASE_DIRECTORY = "test_objects/";
    private static final String EXTENSION = ".json";

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Reads up given test object JSON.
     *
     * @param filename filename (without extension)
     * @param objectDirectory {@link ObjectDirectory} value
     * @param type object type
     * @param <T> type parameter of destination object
     * @return parsed object
     */
    public <T> T read(String filename, ObjectDirectory objectDirectory, Class<T> type) throws IOException {

        Resource inputFile = new ClassPathResource(TEST_OBJECT_BASE_DIRECTORY + objectDirectory.getDirectoryName() + filename + EXTENSION);

        return objectMapper.readValue(inputFile.getInputStream(), type);
    }

    /**
     * Object group directories under TEST_OBJECT_BASE_DIRECTORY directory.
     */
    public enum ObjectDirectory {
        ENTITY("entity/"),
        VO("vo/");

        private String directoryName;

        ObjectDirectory(String directoryName) {
            this.directoryName = directoryName;
        }

        public String getDirectoryName() {
            return directoryName;
        }
    }
}
