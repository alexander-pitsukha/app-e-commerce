package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
@WithMockUser
class FileControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testDownloadFile() throws Exception {
        given(fileService.downloadFile(anyString())).willReturn(any(Resource.class));

        mockMvc.perform(get("/file/download")
                        .param("privateUrl", "photo.jpeg")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService).downloadFile(anyString());
    }

    @Test
    void testUploadProductsFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        willDoNothing().given(fileService).uploadProductsFile(any(MultipartFile.class), anyString());

        mockMvc.perform(multipart("/file/upload/products/image")
                        .file(mockFile)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(fileService).uploadProductsFile(mockFile, null);
    }

    @Test
    void testUploadUsersFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        willDoNothing().given(fileService).uploadUsersFile(any(MultipartFile.class), anyString());

        mockMvc.perform(multipart("/file/upload/users/avatar")
                        .file(mockFile)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(fileService).uploadUsersFile(mockFile, null);
    }

}
