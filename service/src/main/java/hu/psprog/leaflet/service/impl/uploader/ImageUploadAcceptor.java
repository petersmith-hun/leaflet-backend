package hu.psprog.leaflet.service.impl.uploader;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * File uploader component for images.
 * Component accepts the following MIME types:
 *  - image/jpg
 *  - image/jpeg
 *  - image/png
 *  - image/gif
 *
 * @author Peter Smith
 */
@Component
public class ImageUploadAcceptor extends AbstractUploadAcceptor {

    private static final String IMAGE_ROOT_DIRECTORY = "images";
    private static final String ACCEPTOR = "IMAGE";

    private static final String MIME_IMAGE_JPG = "image/jpg";
    private static final String MIME_IMAGE_JPEG = "image/jpeg";
    private static final String MIME_IMAGE_PNG = "image/png";
    private static final String MIME_IMAGE_GIF = "image/gif";

    public ImageUploadAcceptor() {
        super(IMAGE_ROOT_DIRECTORY);
    }

    @Override
    protected List<String> getAcceptedMIMETypes() {
        return Collections.unmodifiableList(Arrays.asList(MIME_IMAGE_JPG, MIME_IMAGE_JPEG, MIME_IMAGE_PNG, MIME_IMAGE_GIF));
    }

    @Override
    protected String acceptedAs() {
        return ACCEPTOR;
    }
}
