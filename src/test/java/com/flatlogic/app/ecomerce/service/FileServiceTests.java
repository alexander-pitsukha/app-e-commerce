package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.repository.FileRepository;
import com.flatlogic.app.ecomerce.service.impl.FileServiceImpl;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@MockBean({FileRepository.class, MessageCodeUtil.class})
class FileServiceTests extends AbstractTests {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MessageCodeUtil messageCodeUtil;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        @Autowired
        public FileService fileService(FileRepository fileRepository, MessageCodeUtil messageCodeUtil) {
            return new FileServiceImpl(fileRepository, messageCodeUtil);
        }
    }

    @Test
    void testDownloadFile() throws Exception {
        String privateUrl = FileService.AVATAR_LOCATION + FileService.FOLDER_SEPARATE + "test.jpeg";
        Path mockPath = mock(Path.class);

        try (MockedStatic<Paths> pathsMockedStatic = mockStatic(Paths.class);
             MockedConstruction<UrlResource> urlResourceMockedConstruction = mockConstruction(UrlResource.class,
                     (mock, context) -> when(mock.exists()).thenReturn(true))) {

            pathsMockedStatic.when(() -> Paths.get(FileService.UPLOAD_LOCATION)).thenReturn(mockPath);
            when(mockPath.resolve(privateUrl)).thenReturn(mockPath);
            when(mockPath.toUri()).thenReturn(any(URI.class));

            fileService.downloadFile(privateUrl);

            pathsMockedStatic.verify(() -> Paths.get(FileService.UPLOAD_LOCATION));
            verify(mockPath).resolve(privateUrl);
            verify(mockPath).toUri();
        }
    }

    @Test
    void testUploadProductsFile() {
        String fileName = FileService.AVATAR_LOCATION + FileService.FOLDER_SEPARATE + "test.jpeg";
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        try (MockedStatic<FileCopyUtils> fileCopyUtilsMockedStatic = mockStatic(FileCopyUtils.class)) {
            fileCopyUtilsMockedStatic.when(() -> FileCopyUtils.copy(file.getBytes(), new File(fileName)))
                    .then(invocationOnMock -> null);

            fileService.uploadUsersFile(file, "test.jpeg");

            fileCopyUtilsMockedStatic.verify(() -> FileCopyUtils.copy(file.getBytes(), new File(fileName)));
        }
    }

    @Test
    void testUploadUsersFile() {
        String fileName = FileService.AVATAR_LOCATION + FileService.FOLDER_SEPARATE + "test.jpeg";
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        try (MockedStatic<FileCopyUtils> fileCopyUtilsMockedStatic = mockStatic(FileCopyUtils.class)) {
            fileCopyUtilsMockedStatic.when(() -> FileCopyUtils.copy(file.getBytes(), new File(fileName)))
                    .then(invocationOnMock -> null);

            fileService.uploadUsersFile(file, "test.jpeg");

            fileCopyUtilsMockedStatic.verify(() -> FileCopyUtils.copy(file.getBytes(), new File(fileName)));
        }
    }

}
