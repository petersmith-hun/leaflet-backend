package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.FileManagementService;
import hu.psprog.leaflet.service.FileMetaInfoService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.FileManagementFacade;
import hu.psprog.leaflet.service.vo.DownloadableFileWrapperVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link FileManagementFacade}.
 *
 * @author Peter Smith
 */
@Service
public class FileManagementFacadeImpl implements FileManagementFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagementFacadeImpl.class);

    @Autowired
    private FileManagementService fileManagementService;

    @Autowired
    private FileMetaInfoService fileMetaInfoService;

    @Override
    public UploadedFileVO upload(FileInputVO fileInputVO) throws ServiceException {

        UploadedFileVO uploadedFileVO = fileManagementService.upload(fileInputVO);
        fileMetaInfoService.storeMetaInfo(uploadedFileVO);

        return uploadedFileVO;
    }

    @Override
    public DownloadableFileWrapperVO download(UUID pathUUID) throws ServiceException {

        UploadedFileVO uploadedFileVO = fileMetaInfoService.retrieveMetaInfo(pathUUID);
        File file = fileManagementService.download(uploadedFileVO.getPath());

        try {
            return wrapFile(uploadedFileVO, file);
        } catch (IOException e) {
            LOGGER.error("Failed to retrieve stored file [{}] for download.", uploadedFileVO.getPath());
            throw new ServiceException("Failed to retrieve stored file for download.", e);
        }
    }

    @Override
    public void remove(UUID pathUUID) throws ServiceException {
        UploadedFileVO uploadedFileVO = fileMetaInfoService.retrieveMetaInfo(pathUUID);
        fileManagementService.remove(uploadedFileVO.getPath());
        fileMetaInfoService.removeMetaInfo(pathUUID);
    }

    @Override
    public void createDirectory(String parent, String directoryName) throws ServiceException {
        fileManagementService.createDirectory(parent, directoryName);
    }

    @Override
    public List<UploadedFileVO> retrieveStoredFileList() {
        return fileMetaInfoService.getUploadedFiles();
    }

    @Override
    public void updateMetaInfo(UUID pathUUID, UpdateFileMetaInfoVO updateFileMetaInfoVO) throws ServiceException {
        fileMetaInfoService.updateMetaInfo(pathUUID, updateFileMetaInfoVO);
    }

    @Override
    public Optional<UploadedFileVO> getCheckedMetaInfo(UUID pathUUID) {
        Optional<UploadedFileVO> uploadedFileVOOptional = Optional.empty();
        try {
            UploadedFileVO uploadedFileVO = fileMetaInfoService.retrieveMetaInfo(pathUUID);
            if (fileManagementService.exists(uploadedFileVO.getPath())) {
                uploadedFileVOOptional = Optional.of(uploadedFileVO);
            }
        } catch (ServiceException e) {
            LOGGER.error("Requested file does not exist", e);
        }
        return uploadedFileVOOptional;
    }

    private DownloadableFileWrapperVO wrapFile(UploadedFileVO metaInfo, File fileToDownload) throws IOException {

        return DownloadableFileWrapperVO.Builder.getBuilder()
                .withOriginalFilename(metaInfo.getOriginalFilename())
                .withMimeType(metaInfo.getAcceptedAs())
                .withLength(fileToDownload.length())
                .withFileContent(convertFileToByteArrayResource(fileToDownload))
                .build();
    }

    private ByteArrayResource convertFileToByteArrayResource(File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        return new ByteArrayResource(Files.readAllBytes(path));
    }
}
