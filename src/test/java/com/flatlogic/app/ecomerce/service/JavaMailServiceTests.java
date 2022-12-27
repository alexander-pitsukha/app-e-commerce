package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.service.impl.JavaMailServiceImpl;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@MockBean({JavaMailSender.class, MessageCodeUtil.class})
class JavaMailServiceTests extends AbstractTests {

    private static final String EMAIL = "admin@flatlogic.com";

    @Autowired
    private JavaMailService javaMailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageCodeUtil messageCodeUtil;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        @Autowired
        public JavaMailService javaMailService(JavaMailSender javaMailSender, MessageCodeUtil messageCodeUtil) {
            return new JavaMailServiceImpl(javaMailSender, messageCodeUtil);
        }
    }

    @Test
    void testSendEmailForCreateUser() {
        UUID uuid = UUID.randomUUID();
        String resetUrl = String.format("${frontend.host}/#/verify-email?token=%s", uuid);
        MimeMessage message = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(message);
        when(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_SUBJECT,
                new Object[]{JavaMailService.APPLICATION})).thenReturn(JavaMailService.APPLICATION);
        when(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_BODY,
                new Object[]{resetUrl, resetUrl, JavaMailService.APPLICATION})).thenReturn(JavaMailService.APPLICATION);
        doNothing().when(javaMailSender).send(message);

        javaMailService.sendEmailForCreateUser(EMAIL, uuid);

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(message);
    }

    @Test
    void sendEmailForUpdateUserPasswordResetToken() {
        UUID uuid = UUID.randomUUID();
        String resetUrl = String.format("${frontend.host}/#/password-reset?token=%s", uuid);
        MimeMessage message = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(message);
        when(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_RESET_PASSWORD_SUBJECT,
                new Object[]{JavaMailService.APPLICATION})).thenReturn(JavaMailService.APPLICATION);
        when(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_RESET_PASSWORD_BODY,
                new Object[]{JavaMailService.APPLICATION, EMAIL, resetUrl, resetUrl, JavaMailService.APPLICATION}))
                .thenReturn(JavaMailService.APPLICATION);
        doNothing().when(javaMailSender).send(message);

        javaMailService.sendEmailForUpdateUserPasswordResetToken(EMAIL, uuid);

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(message);
    }

}
