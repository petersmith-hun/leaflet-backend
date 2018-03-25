package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.impl.uploader.FileUploader;
import hu.psprog.leaflet.service.impl.uploader.acceptor.UploadAcceptor;
import hu.psprog.leaflet.service.security.annotation.PermitEditorOrAdmin;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FileManagementService}.
 *
 * @author Peter Smith
 */
@Service
public class FileManagementServiceImpl implements FileManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagementServiceImpl.class);
    private static final String GIVEN_PATH_IS_INVALID_MESSAGE_WITH_PATH = "Given path [{}] is invalid";
    private static final String GIVEN_PATH_IS_INVALID = "Given path is invalid";

    private File fileStorage;
    private FileUploader fileUploader;
    private List<UploadAcceptor> uploadAcceptors;

    @Autowired
    public FileManagementServiceImpl(File fileStorage, FileUploader fileUploader, List<UploadAcceptor> uploadAcceptors) {
        this.fileStorage = fileStorage;
        this.fileUploader = fileUploader;
        this.uploadAcceptors = uploadAcceptors;
    }

    @Override
    @PermitEditorOrAdmin
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
            LOGGER.error(GIVEN_PATH_IS_INVALID_MESSAGE_WITH_PATH, path);
            throw new ServiceException(GIVEN_PATH_IS_INVALID, exc);
        }
    }

    @Override
    @PermitEditorOrAdmin
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
            LOGGER.info("File [{}] has been removed", file.getAbsolutePath());
        } catch (InvalidPathException exc) {
            LOGGER.error(GIVEN_PATH_IS_INVALID_MESSAGE_WITH_PATH, path);
            throw new ServiceException(GIVEN_PATH_IS_INVALID, exc);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public void createDirectory(String parent, String directoryName) throws ServiceException {
        try {
            Path path = Paths.get(parent, directoryName);
            File file = buildAbsolutePath(path.toString()).toFile();
            if (!file.mkdir()) {
                LOGGER.error("Failed to create directory [{}]", file.getAbsolutePath());
                throw new ServiceException("Failed to create directory");
            }
            LOGGER.info("Created directory [{}]", file.getAbsolutePath());
        } catch (InvalidPathException exc) {
            LOGGER.error(GIVEN_PATH_IS_INVALID_MESSAGE_WITH_PATH, parent);
            throw new ServiceException(GIVEN_PATH_IS_INVALID, exc);
        }
    }

    @Override
    public boolean exists(String path) throws ServiceException {
        try {
            File file = buildAbsolutePath(path).toFile();
            return isAccessible(file);
        } catch (InvalidPathException exc) {
            LOGGER.error(GIVEN_PATH_IS_INVALID_MESSAGE_WITH_PATH, path);
            throw new ServiceException(GIVEN_PATH_IS_INVALID, exc);
        }
    }

    @Override
    @PermitEditorOrAdmin
    public List<AcceptorInfoVO> getAcceptorInfo() {

        return uploadAcceptors.stream()
                .map(this::extractUploadAcceptorInfo)
                .collect(Collectors.toList());
    }

    private AcceptorInfoVO extractUploadAcceptorInfo(UploadAcceptor uploadAcceptor) {
        return AcceptorInfoVO.getBuilder()
                .withId(uploadAcceptor.acceptedAs())
                .withRootDirectoryName(uploadAcceptor.groupRootDirectory())
                .withChildrenDirectories(getChildrenDirectories(uploadAcceptor))
                .withAcceptableMimeTypes(uploadAcceptor.getAcceptedMIMETypes().stream()
                        .map(MimeType::toString)
                        .collect(Collectors.toList()))
                .build();
    }

    private List<String> getChildrenDirectories(UploadAcceptor uploadAcceptor) {

        List<String> childrenDirectories = null;
        try {
            Path acceptorRootPath = buildAbsolutePath(uploadAcceptor.groupRootDirectory());
            childrenDirectories = Files.walk(acceptorRootPath)
                    .filter(path -> path.toFile().isDirectory())
                    .map(acceptorRootPath::relativize)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Unable to retrieve acceptor root information.", e);
        }

        return Optional.ofNullable(childrenDirectories)
                .orElse(Collections.emptyList());
    }

    private boolean isAccessible(File file) {
        return file.exists() && file.canRead();
    }

    private Path buildAbsolutePath(String path) {
        return Paths.get(fileStorage.getAbsolutePath(), path);
    }
}
