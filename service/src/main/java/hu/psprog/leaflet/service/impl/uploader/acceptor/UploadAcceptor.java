package hu.psprog.leaflet.service.impl.uploader.acceptor;

import hu.psprog.leaflet.service.vo.FileInputVO;

/**
 * Upload acceptors decide whether to accept of reject the file being uploaded.
 * Decision is based on the file's MIME type.
 * Acceptors also suggests subdirectories under file storage as a file group storage root.
 *
 * @author Peter Smith
 */
public interface UploadAcceptor {

    /**
     * Decides whether given file is accepted or rejected.
     *
     * @param fileInputVO {@link FileInputVO} object to extract file's MIME type
     * @return {@code true} if file is accepted by the acceptor, {@code false} otherwise
     */
    boolean accept(FileInputVO fileInputVO);

    /**
     * Returns acceptor's identifier.
     *
     * @return acceptor identifier
     */
    String acceptedAs();

    /**
     * Acceptable file group's suggested subdirectory.
     * It's the uploader component's responsibility to use this directory for uploads, though it can ignore it.
     *
     * @return suggested subdirectory name
     */
    String groupRootDirectory();
}
