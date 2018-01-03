package hu.psprog.leaflet.service.impl.uploader.acceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.List;

/**
 * File uploader component for images.
 * Component accepts the following MIME types:
 *  - image/*
 *
 * @author Peter Smith
 */
@Component
public class ImageUploadAcceptor extends AbstractUploadAcceptor implements UploadAcceptor {

    private static final String IMAGE_ROOT_DIRECTORY = "images";
    private static final String ACCEPTOR = "IMAGE";

    private static final MimeType MIME_IMAGE_ALL = MimeType.valueOf("image/*");

    @Override
    public String acceptedAs() {
        return ACCEPTOR;
    }

    @Override
    public String groupRootDirectory() {
        return IMAGE_ROOT_DIRECTORY;
    }

    @Override
    public List<MimeType> getAcceptedMIMETypes() {
        return Collections.unmodifiableList(Collections.singletonList(MIME_IMAGE_ALL));
    }
}
