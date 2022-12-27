package com.flatlogic.app.ecomerce.service.impl;

import com.flatlogic.app.ecomerce.exception.SendMailException;
import com.flatlogic.app.ecomerce.service.JavaMailService;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * JavaMailService service.
 */
@Service
@RequiredArgsConstructor
public class JavaMailServiceImpl implements JavaMailService {

    private final JavaMailSender javaMailSender;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * Email from variable.
     */
    @Value("${email.from}")
    private String emailFrom;

    /**
     * Frontend host variable.
     */
    @Value("${frontend.host}")
    private String frontendHost;

    @Override
    public void sendEmailForCreateUser(final String email, final UUID token) {
        var message = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_SUBJECT, new Object[]{APPLICATION}));
            var resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/verify-email")
                    .queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_VERIFY_EMAIL_BODY,
                    new Object[]{resetUrl, resetUrl, APPLICATION}), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED));
        }
    }

    @Override
    public void sendEmailForUpdateUserPasswordResetToken(final String email, final UUID token) {
        var message = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_AUTH_MAIL_RESET_PASSWORD_SUBJECT, new Object[]{APPLICATION}));
            var resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/password-reset")
                    .queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_RESET_PASSWORD_BODY,
                    new Object[]{APPLICATION, email, resetUrl, resetUrl, APPLICATION}), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_PASSWORD_RESET_SEND_MESSAGE));
        }
    }

}
