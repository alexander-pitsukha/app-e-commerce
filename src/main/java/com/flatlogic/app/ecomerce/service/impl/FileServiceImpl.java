package com.flatlogic.app.ecomerce.service.impl;

import com.flatlogic.app.ecomerce.exception.FileException;
import com.flatlogic.app.ecomerce.repository.FileRepository;
import com.flatlogic.app.ecomerce.service.FileService;
import com.flatlogic.app.ecomerce.type.BelongsToType;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * FileService service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * PostConstruct method.
     */
    @PostConstruct
    public void init() {
        try {
            var root = Paths.get(UPLOAD_LOCATION);
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
            var product = Paths.get(PRODUCT_LOCATION);
            if (Files.notExists(product)) {
                Files.createDirectory(product);
            }
            var user = Paths.get(USER_LOCATION);
            if (Files.notExists(user)) {
                Files.createDirectory(user);
            }
            var image = Paths.get(IMAGE_LOCATION);
            if (Files.notExists(image)) {
                Files.createDirectory(image);
            }
            var avatar = Paths.get(AVATAR_LOCATION);
            if (Files.notExists(avatar)) {
                Files.createDirectory(avatar);
            }
        } catch (IOException e) {
            throw new FileException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_CREATE_FOLDER));
        }
    }

    /**
     * Download file.
     *
     * @param privateUrl File private url
     * @return Resource
     */
    @Override
    public Resource downloadFile(final String privateUrl) {
        try {
            Path file = Paths.get(UPLOAD_LOCATION).resolve(privateUrl);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
            }
        } catch (MalformedURLException e) {
            throw new FileException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
        }
    }

    /**
     * Upload products file.
     *
     * @param file     MultipartFile
     * @param filename File name
     */
    @Override
    public void uploadProductsFile(final MultipartFile file, final String filename) {
        try {
            FileCopyUtils.copy(file.getBytes(),
                    new File(IMAGE_LOCATION + FOLDER_SEPARATE + filename));
        } catch (IOException e) {
            throw new FileException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_UPLOAD_FILE, new Object[]{filename}));
        }
    }

    /**
     * Upload users file.
     *
     * @param file     MultipartFile
     * @param filename File name
     */
    @Override
    public void uploadUsersFile(final MultipartFile file, final String filename) {
        try {
            FileCopyUtils.copy(file.getBytes(),
                    new File(AVATAR_LOCATION + FOLDER_SEPARATE + filename));
        } catch (IOException e) {
            throw new FileException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_UPLOAD_FILE, new Object[]{filename}));
        }
    }

    /**
     * Remove legacy files.
     */
    @Scheduled(cron = "${scheduled.remove.legacy.files}")
    public void removeLegacyFiles() {
        log.info("Remove legacy files.");
        Arrays.asList(IMAGE_LOCATION, AVATAR_LOCATION).forEach(path -> {
            removeFilesFromDisk(path);
            removeEntriesFromTable(path);
        });
    }

    private void removeFilesFromDisk(String path) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(path))) {
            pathStream.filter(Files::isRegularFile).forEach(file -> {
                boolean result = fileRepository.existsByPrivateUrl(file.toString()
                        .substring(UPLOAD_LOCATION.length() + 1));
                if (!result) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void removeEntriesFromTable(String path) {
        List<com.flatlogic.app.ecomerce.entity.File> files = fileRepository.findAllByBelongsTo(
                Objects.equals(path, IMAGE_LOCATION) ? BelongsToType.PRODUCTS.getType()
                        : BelongsToType.USERS.getType());
        files.forEach(entry -> {
            try (Stream<Path> pathStream = Files.walk(Paths.get(path))) {
                Optional<Path> any = pathStream.filter(file -> Files.isRegularFile(file) &&
                        Objects.equals(entry.getPrivateUrl(), file.toString()
                                .substring(UPLOAD_LOCATION.length() + 1))).findAny();
                if (any.isEmpty()) {
                    fileRepository.delete(entry);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
