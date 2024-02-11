package com.flatlogic.app.ecomerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.AppECommerceApplication;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.controller.request.UserRequest;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.exception.ValidationException;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppECommerceApplication.class)
class UserServiceTests extends AbstractServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageCodeUtil messageCodeUtil;

    @MockBean
    private JavaMailService javaMailService;

    @Test
    void testGetUsersThreeParamsIsNull() {
        List<User> users = userService.getUsers(null, null, null);

        assertEquals(3, users.size());
    }

    @Test
    void testGetUsersThreeParamsIsNotNull() {
        List<User> users = userService.getUsers(0, 1, "email_DESC");

        assertEquals(1, users.size());
    }

    @Test
    void testGetUsersTwoParamsIsNull() {
        List<User> users = userService.getUsers(null, 2);

        assertEquals(2, users.size());
    }

    @Test
    void testGetUsersTwoParamsIsNotNull() {
        List<User> users = userService.getUsers("admin", 2);

        assertEquals(1, users.size());
    }

    @Test
    void testGetUserByEmail() {
        User user = userService.getUserByEmail("admin@flatlogic.com");

        assertNotNull(user);
    }

    @Test
    void testGetUserById() {
        User user = userService.getUserById(UUID.fromString("52bc1fb6-0fe5-4647-8cbc-8c55e156b889"));

        assertNotNull(user);
    }

    @Test
    void testCreateUserAndSendEmail() {
        doNothing().when(javaMailService).sendEmailForCreateUser(anyString(), any(UUID.class));

        User user = userService.createUserAndSendEmail("user2@flatlogic.com", UUID.randomUUID().toString());

        assertNotNull(user);

        verify(javaMailService).sendEmailForCreateUser(anyString(), any(UUID.class));
    }

    @Test
    void testUpdateEmailVerification() throws IOException {
        User user = getObjectFromJson("/json/user_2.json", User.class);
        user.setEmailVerificationTokenExpiresAt(LocalDateTime.now());
        user = userRepository.save(user);

        user = userService.updateEmailVerification(user.getEmailVerificationToken());

        assertNull(user.getEmailVerificationToken());
        assertNull(user.getEmailVerificationTokenExpiresAt());
    }

    @Test
    void testUpdateEmailVerificationException() throws IOException {
        User user = getObjectFromJson("/json/user_2.json", User.class);
        user.setEmailVerificationTokenExpiresAt(LocalDateTime.now().minusYears(1));
        userRepository.save(user);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.updateEmailVerification(user.getEmailVerificationToken()));
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                Constants.ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED), exception.getMessage());
    }

    @Test
    void testSaveUser() throws IOException {
        RequestData<UserRequest> requestData = getObjectFromJson("json/user_request_1.json", new TypeReference<>() {
        });

        User user = userService.saveUser(requestData.getData());

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(requestData.getData().getFirstName(), user.getFirstName());
        assertEquals(requestData.getData().getLastName(), user.getLastName());
        assertEquals(requestData.getData().getPhoneNumber(), user.getPhoneNumber());
        assertEquals(requestData.getData().getEmail(), user.getEmail());
        assertEquals(requestData.getData().getRole(), user.getRole().getAuthority());
        assertEquals(requestData.getData().isDisabled(), user.getDisabled());
        assertEquals(requestData.getData().getPassword(), user.getPassword());
        assertEquals(requestData.getData().isEmailVerified(), user.getEmailVerified());
        assertEquals(requestData.getData().getEmailVerificationToken(), user.getEmailVerificationToken());
        assertEquals(requestData.getData().getEmailVerificationTokenExpiresAt(), user.getEmailVerificationTokenExpiresAt());
        assertEquals(requestData.getData().getPasswordResetToken(), user.getPasswordResetToken());
        assertEquals(requestData.getData().getPasswordResetTokenExpiresAt(), user.getPasswordResetTokenExpiresAt());
        assertEquals(requestData.getData().getEmailVerificationTokenExpiresAt(), user.getEmailVerificationTokenExpiresAt());
        assertEquals(requestData.getData().getProvider(), user.getProvider());
        assertEquals(requestData.getData().getImportHash(), user.getImportHash());
        assertEquals(requestData.getData().getProductIds().size(), user.getProducts().size());
        assertEquals(requestData.getData().getFileRequests().size(), user.getFiles().size());
    }

    @Test
    void testUpdateUser() throws IOException {
        RequestData<UserRequest> requestData = getObjectFromJson("json/user_request_2.json", new TypeReference<>() {
        });

        User user = userService.updateUser(UUID.fromString("127b7b84-5af0-4e66-93ca-346ac35e1bdd"), requestData.getData());

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(requestData.getData().getFirstName(), user.getFirstName());
        assertEquals(requestData.getData().getLastName(), user.getLastName());
        assertEquals(requestData.getData().getPhoneNumber(), user.getPhoneNumber());
        assertEquals(requestData.getData().getEmail(), user.getEmail());
        assertEquals(requestData.getData().getRole(), user.getRole().getAuthority());
        assertEquals(requestData.getData().isDisabled(), user.getDisabled());
        assertEquals(requestData.getData().getPassword(), user.getPassword());
        assertEquals(requestData.getData().isEmailVerified(), user.getEmailVerified());
        assertEquals(requestData.getData().getEmailVerificationToken(), user.getEmailVerificationToken());
        assertEquals(requestData.getData().getEmailVerificationTokenExpiresAt(), user.getEmailVerificationTokenExpiresAt());
        assertEquals(requestData.getData().getPasswordResetToken(), user.getPasswordResetToken());
        assertEquals(requestData.getData().getPasswordResetTokenExpiresAt(), user.getPasswordResetTokenExpiresAt());
        assertEquals(requestData.getData().getEmailVerificationTokenExpiresAt(), user.getEmailVerificationTokenExpiresAt());
        assertEquals(requestData.getData().getProvider(), user.getProvider());
        assertEquals(requestData.getData().getImportHash(), user.getImportHash());
        assertEquals(requestData.getData().getProductIds().size(), user.getProducts().size());
        assertEquals(requestData.getData().getFileRequests().size(), user.getFiles().size());
    }

    @Test
    void testDeleteUser() {
        UUID uuid = UUID.fromString("127b7b84-5af0-4e66-93ca-346ac35e1bdd");

        userService.deleteUser(uuid);
        User user = userService.getUserById(uuid);

        assertNotNull(user.getDeletedAt());
    }

}
