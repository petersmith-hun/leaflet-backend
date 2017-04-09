package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.uploader.AbstractUploadAcceptor;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link FileManagementService}.
 *
 * @author Peter Smith
 */
@Service
public class FileManagementServiceImpl implements FileManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagementServiceImpl.class);

    @Autowired
    private List<AbstractUploadAcceptor> uploaders;

    @Autowired
    private File fileStorage;

    @Override
    public UploadedFileVO upload(FileInputVO fileInputVO) throws ServiceException {

        try {
            UploadedFileVO uploadedFileVO = null;
            Iterator<AbstractUploadAcceptor> uploaderIterator = uploaders.iterator();
            while (Objects.isNull(uploadedFileVO) && uploaderIterator.hasNext()) {
                uploadedFileVO = uploaderIterator.next().upload(fileInputVO);
            }

            if (Objects.isNull(uploadedFileVO)) {
                LOGGER.error("No acceptor for MIME [{}] found to upload file [{}]",
                        fileInputVO.getOriginalFilename(), fileInputVO.getContentType());
                throw new ServiceException("No acceptor found to upload file");
            }

            return uploadedFileVO;
        } catch (FileUploadException exc) {
            LOGGER.error("Failed to upload file", exc);
            throw new ServiceException("Failed to upload file", exc);
        }
    }

    @Override
    public OutputStream download(String filename) throws ServiceException {
        return null;
    }

    @Override
    public void remove(String filename) throws ServiceException {

    }

    @Override
    public void createDirectory(String parent, String directoryName) {

    }
}
