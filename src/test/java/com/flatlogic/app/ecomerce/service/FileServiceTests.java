package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AppECommerceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppECommerceApplication.class)
class FileServiceTests extends AbstractServiceTests {

    @Autowired
    private FileService fileService;

    @Test
    void testDownloadFile() {
    }

    @Test
    void testUploadProductsFile() {
    }

    @Test
    void testUploadUsersFile() {
    }

}
