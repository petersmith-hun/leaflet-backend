package hu.psprog.leaflet.service.impl.uploader;

import hu.psprog.leaflet.service.exception.DirectoryCreationException;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Abstract file upload handler.
 * Contains common operations (eg.: the upload itself) and abstract specifications for the concrete handlers to implement.
 *
 * @author Peter Smith
 */
public abstract class AbstractUploadAcceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUploadAcceptor.class);

    @Autowired
    private File fileStorage;

    private String fileGroupDirectory;
    private File acceptorRoot;

    public AbstractUploadAcceptor(@NotNull String fileGroupDirectory) {
        this.fileGroupDirectory = fileGroupDirectory;
    }

    @PostConstruct
    public void initializeAcceptorRoot() throws DirectoryCreationException {
        Path path = Paths.get(fileStorage.getAbsolutePath(), fileGroupDirectory);
        acceptorRoot = path.toFile();
        if (!acceptorRoot.exists()) {
            if (!acceptorRoot.mkdir()) {
                LOGGER.error("Acceptor root could not be created at [{}]", path.toAbsolutePath());
                throw new DirectoryCreationException("Acceptor root could not be created at [{}]");
            } else {
                LOGGER.info("Acceptor root created at [{}]", path.toAbsolutePath());
            }
        } else {
            LOGGER.info("Existing acceptor root attached at [{}]", path.toAbsolutePath());
        }
    }

    public UploadedFileVO upload(FileInputVO fileInputVO) throws FileUploadException {

        try {
            UploadedFileVO uploadedFileVO = null;
            if (isAccepted(fileInputVO)) {
                uploadedFileVO = doUpload(fileInputVO);
            }
            return uploadedFileVO;
        } catch (IOException exc) {
            LOGGER.error("Could not move uploaded file [{}] to destination location", fileInputVO.getOriginalFilename());
            throw new FileUploadException("Could not move uploaded file to destination location", exc);
        } catch (InvalidPathException exc) {
            LOGGER.error("Given path is invalid", exc);
            throw new FileUploadException("Given path is invalid", exc);
        }
    }

    private boolean isAccepted(FileInputVO fileInputVO) {
        return getAcceptedMIMETypes().contains(fileInputVO.getContentType());
    }

    private UploadedFileVO doUpload(FileInputVO fileInputVO) throws IOException {
        Path path = buildPath(fileInputVO);
        String targetFilename = cleanFilename(fileInputVO);
        Files.copy(fileInputVO.getFileContentStream(), path.resolve(targetFilename));

        return UploadedFileVO.Builder.getBuilder()
                .withOriginalFilename(fileInputVO.getOriginalFilename())
                .withStoredFilename(targetFilename)
                .withPath(path.toAbsolutePath().toString())
                .withAcceptedAs(acceptedAs())
                .build();
    }

    private String cleanFilename(FileInputVO fileInputVO) {
        return StringUtils.stripAccents(fileInputVO.getOriginalFilename())
                .toLowerCase()
                .replace(' ', '_')
                .trim();
    }

    private Path buildPath(FileInputVO fileInputVO) throws InvalidPathException {
        Path path;
        if (Objects.nonNull(fileInputVO.getRelativePath())) {
            path = Paths.get(acceptorRoot.getAbsolutePath(), fileInputVO.getRelativePath());
        } else {
            path = Paths.get(acceptorRoot.getAbsolutePath());
        }
        return path;
    }

    protected abstract List<String> getAcceptedMIMETypes();

    protected abstract String acceptedAs();
}
