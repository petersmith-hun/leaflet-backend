package hu.psprog.leaflet.service.impl.uploader;

import hu.psprog.leaflet.service.exception.DirectoryCreationException;
import hu.psprog.leaflet.service.exception.FileUploadException;
import hu.psprog.leaflet.service.impl.uploader.acceptor.UploadAcceptor;
import hu.psprog.leaflet.service.util.FilenameGeneratorUtil;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * File upload logic.
 *
 * @author Peter Smith
 */
@Component
public class FileUploader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploader.class);

    private static final String GIVEN_PATH_IS_INVALID = "Given path is invalid";
    private static final String CHARACTER_BACKSLASH = "\\";
    private static final String CHARACTER_SLASH = "/";

    private FilenameGeneratorUtil filenameGeneratorUtil;
    private File fileStorage;
    private Map<Class<? extends UploadAcceptor>, UploadAcceptor> uploadAcceptorMap;
    private Map<Class<? extends UploadAcceptor>, File> acceptorRootMap;

    private Function<UploadAcceptor, File> mapUploadAcceptorToRootDirectory = uploadAcceptor -> {
        Path path = Paths.get(fileStorage.getAbsolutePath(), uploadAcceptor.groupRootDirectory());
        File acceptorRoot = path.toFile();
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
        return acceptorRoot;
    };

    @Autowired
    public FileUploader(final File fileStorage, final List<UploadAcceptor> uploadAcceptors,
                        final FilenameGeneratorUtil filenameGeneratorUtil) {
        this.fileStorage = fileStorage;
        this.filenameGeneratorUtil = filenameGeneratorUtil;
        initializeAcceptorMap(uploadAcceptors);
        initializeAcceptorRoots();
    }

    public UploadedFileVO upload(FileInputVO fileInputVO) {

        UploadedFileVO uploadedFileVO = null;
        UploadAcceptor currentAcceptor;
        Iterator<UploadAcceptor> uploadAcceptorIterator = uploadAcceptorMap.values().iterator();
        try {
            while (Objects.isNull(uploadedFileVO) && uploadAcceptorIterator.hasNext()) {
                currentAcceptor = uploadAcceptorIterator.next();
                if (currentAcceptor.accept(fileInputVO)) {
                    uploadedFileVO = doUpload(fileInputVO, uploadAcceptorMap.get(currentAcceptor.getClass()));
                }
            }
        } catch (IOException exc) {
            LOGGER.error("Could not move uploaded file [{}] to destination location", fileInputVO.getOriginalFilename());
            throw new FileUploadException("Could not move uploaded file to destination location", exc);
        } catch (InvalidPathException exc) {
            LOGGER.error(GIVEN_PATH_IS_INVALID, exc);
            throw new FileUploadException(GIVEN_PATH_IS_INVALID, exc);
        }
        return uploadedFileVO;
    }

    private void initializeAcceptorMap(List<UploadAcceptor> uploadAcceptors) {
        uploadAcceptorMap = uploadAcceptors.stream()
                .collect(Collectors.toMap(UploadAcceptor::getClass, Function.identity()));
    }

    private void initializeAcceptorRoots() {
        acceptorRootMap = uploadAcceptorMap.values().stream()
                .collect(Collectors.toMap(UploadAcceptor::getClass, mapUploadAcceptorToRootDirectory));
    }

    private UploadedFileVO doUpload(FileInputVO fileInputVO, UploadAcceptor uploadAcceptor) throws IOException {
        Path path = buildPath(fileInputVO, uploadAcceptor);
        String targetFilename = filenameGeneratorUtil.cleanFilename(fileInputVO);
        String fileRelativePath = getNormalizedPathAsString(buildRelativePath(fileInputVO, targetFilename, uploadAcceptor));
        Files.copy(fileInputVO.getFileContentStream(), path.resolve(targetFilename));

        return UploadedFileVO.getBuilder()
                .withOriginalFilename(fileInputVO.getOriginalFilename())
                .withPath(fileRelativePath)
                .withAcceptedAs(fileInputVO.getContentType())
                .withStoredFilename(targetFilename)
                .withPathUUID(generatePathUUID(fileRelativePath))
                .withDescription(fileInputVO.getDescription())
                .build();
    }

    private UUID generatePathUUID(String path) {
        return UUID.nameUUIDFromBytes(path.getBytes());
    }

    private String getNormalizedPathAsString(Path path) {
        return path.toString().replace(CHARACTER_BACKSLASH, CHARACTER_SLASH);
    }

    private Path buildPath(FileInputVO fileInputVO, UploadAcceptor uploadAcceptor) {
        Path path;
        String acceptorRootPath = getAcceptorRoot(uploadAcceptor).getAbsolutePath();
        if (Objects.nonNull(fileInputVO.getRelativePath())) {
            path = Paths.get(acceptorRootPath, fileInputVO.getRelativePath());
        } else {
            path = Paths.get(acceptorRootPath);
        }
        return path;
    }

    private Path buildRelativePath(FileInputVO fileInputVO, String filename, UploadAcceptor uploadAcceptor) {
        Path path;
        String acceptorRootName = getAcceptorRoot(uploadAcceptor).getName();
        if (Objects.nonNull(fileInputVO.getRelativePath())) {
            path = Paths.get(acceptorRootName, fileInputVO.getRelativePath(), filename);
        } else {
            path = Paths.get(acceptorRootName, filename);
        }
        return path;
    }

    private File getAcceptorRoot(UploadAcceptor uploadAcceptor) {
        return acceptorRootMap.get(uploadAcceptor.getClass());
    }
}
