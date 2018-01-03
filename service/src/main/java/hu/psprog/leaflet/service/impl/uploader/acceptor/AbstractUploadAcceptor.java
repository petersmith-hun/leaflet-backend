package hu.psprog.leaflet.service.impl.uploader.acceptor;

import hu.psprog.leaflet.service.vo.FileInputVO;
import org.springframework.util.MimeType;

/**
 * Common implementation for {@code accept} method.
 *
 * @author Peter Smith
 */
public abstract class AbstractUploadAcceptor implements UploadAcceptor {

    @Override
    public boolean accept(FileInputVO fileInputVO) {
        MimeType mimeType = getMimeType(fileInputVO);
        return getAcceptedMIMETypes().stream()
                .anyMatch(mimeType::isCompatibleWith);
    }

    private MimeType getMimeType(FileInputVO fileInputVO) {
        return MimeType.valueOf(fileInputVO.getContentType());
    }
}
