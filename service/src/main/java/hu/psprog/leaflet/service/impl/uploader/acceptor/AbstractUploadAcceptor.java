package hu.psprog.leaflet.service.impl.uploader.acceptor;

import hu.psprog.leaflet.service.vo.FileInputVO;

import java.util.List;

/**
 * Common implementation for {@code accept} method.
 *
 * @author Peter Smith
 */
public abstract class AbstractUploadAcceptor implements UploadAcceptor {

    @Override
    public boolean accept(FileInputVO fileInputVO) {
        return getAcceptedMIMETypes().contains(fileInputVO.getContentType());
    }

    protected abstract List<String> getAcceptedMIMETypes();
}
