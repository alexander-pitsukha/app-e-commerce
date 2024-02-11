package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AppECommerceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppECommerceApplication.class)
class JavaMailServiceTests extends AbstractServiceTests {

    @Autowired
    private JavaMailService javaMailService;

    @Test
    void testSendEmailForCreateUser() {
    }

    @Test
    void testSendEmailForUpdateUserPasswordResetToken() {
    }

}
