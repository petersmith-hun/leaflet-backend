package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.uploader.FileUploader;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Implementation of {@link FileManagementService}.
 *
 * @author Peter Smith
 */
@Service
public class FileManagementServiceImpl implements FileManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagementServiceImpl.class);

    private File fileStorage;
    private FileUploader fileUploader;

    @Autowired
    public FileManagementServiceImpl(File fileStorage, FileUploader fileUploader) {
        this.fileStorage = fileStorage;
        this.fileUploader = fileUploader;
    }

    @Override
    public UploadedFileVO upload(FileInputVO fileInputVO) throws ServiceException {

        Assert.state(fileInputVO.getSize() > 0, "File size must be greater than 0!");

        UploadedFileVO uploadedFileVO;
        try {
            uploadedFileVO = fileUploader.upload(fileInputVO);
        } catch (FileUploadException exc) {
            LOGGER.error("Failed to upload file", exc);
            throw new ServiceException("Failed to upload file", exc);
        }

        if (Objects.isNull(uploadedFileVO)) {
            LOGGER.error("No acceptor for MIME [{}] found to upload file [{}]",
                    fileInputVO.getContentType(), fileInputVO.getOriginalFilename());
            throw new ServiceException("No acceptor found to upload file");
        }

        return uploadedFileVO;
    }

    @Override
    public File download(String path) throws ServiceException {
        try {
            File file = buildAbsolutePath(path).toFile();
            if (!isAccessible(file)) {
                LOGGER.error("File [{}] is not accessible", file.getAbsolutePath());
                throw new ServiceException("File is not accessible");
            }
            return file;
        } catch (InvalidPathException exc) {
            LOGGER.error("Given path [{}] is invalid", path);
            throw new ServiceException("Given path is invalid", exc);
        }
    }

    @Override
    public void remove(String path) throws ServiceException {
        try {
            File file = buildAbsolutePath(path).toFile();
            if (!isAccessible(file)) {
                LOGGER.error("File [{}] is not accessible", file.getAbsolutePath());
                throw new ServiceException("File is not accessible");
            }
            if (!file.delete()) {
                LOGGER.error("Failed to remove file [{}]", file.getAbsoluteFile());
                throw new ServiceException("Failed to remove file");
            }
        } catch (InvalidPathException exc) {
            LOGGER.error("Given path [{}] is invalid", path);
            throw new ServiceException("Given path is invalid", exc);
        }
    }

    @Override
    public void createDirectory(String parent, String directoryName) throws ServiceException {
        try {
            Path path = Paths.get(parent, directoryName);
            File file = buildAbsolutePath(path.toString()).toFile();
            if (!file.mkdir()) {
                LOGGER.error("Failed to create directory [{}]", file.getAbsolutePath());
                throw new ServiceException("Failed to create directory");
            }
        } catch (InvalidPathException exc) {
            LOGGER.error("Given path [{}] is invalid", parent);
            throw new ServiceException("Given path is invalid", exc);
        }
    }

    @Override
    public boolean exists(String path) throws ServiceException {
        try {
            File file = buildAbsolutePath(path).toFile();
            return isAccessible(file);
        } catch (InvalidPathException exc) {
            LOGGER.error("Given path [{}] is invalid", path);
            throw new ServiceException("Given path is invalid", exc);
        }
    }

    private boolean isAccessible(File file) {
        return file.exists() && file.canRead();
    }

    private Path buildAbsolutePath(String path) {
        return Paths.get(fileStorage.getAbsolutePath(), path);
    }
}
