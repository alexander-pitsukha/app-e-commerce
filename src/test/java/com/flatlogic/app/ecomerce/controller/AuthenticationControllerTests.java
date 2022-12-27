package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.controller.request.*;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.service.UserService;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@WithMockUser
class AuthenticationControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCache userCache;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MessageCodeUtil messageCodeUtil;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetCurrentUser() throws Exception {
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.getUserByEmail(anyString())).willReturn(user);

        mockMvc.perform(get("/auth/me")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.role", is(user.getRole().getAuthority())))
                .andExpect(jsonPath("$.disabled", is(user.getDisabled())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.emailVerified", is(user.getEmailVerified())))
                .andExpect(jsonPath("$.emailVerificationToken", is(user.getEmailVerificationToken())))
                .andExpect(jsonPath("$.emailVerificationTokenExpiresAt", is(user.getEmailVerificationTokenExpiresAt())))
                .andExpect(jsonPath("$.passwordResetToken", is(user.getPasswordResetToken())))
                .andExpect(jsonPath("$.passwordResetTokenExpiresAt", is(user.getPasswordResetTokenExpiresAt())))
                .andExpect(jsonPath("$.provider", is(user.getProvider())))
                .andExpect(jsonPath("$.importHash", is(user.getImportHash())))
                .andExpect(jsonPath("$.wishlist", hasSize(user.getProducts().size())))
                .andExpect(jsonPath("$.avatar", hasSize(user.getFiles().size())));

        verify(userService).getUserByEmail(anyString());
    }

    @Test
    void testLocalLogin() throws Exception {
        AuthRequest authRequest = getObjectFromJson("json/auth_request.json", AuthRequest.class);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJFQUF5bURwWFhUU0FCQUxyejd1dk5VUzV2d0lqY0JUVDFBNzRTOG1aQXU2T2RhY0t2ckMxRzVLSmQ3Z0pGWkNkWkM4ejBqeXY0dEFrbUZoY2FpTDVUM1RLRHRiWkN4VFpCQnZnZ0dQbm9DSG5aQUt0dUtNdldPTXg3eVVXNnpETjZDZHFBM05VTDVrMnlQaGxvR0cxcEdTTGsxenZ1c0xvN0E1WkNLeDNRZjE1OW1Gd0NLQ3MwTGNTcThDVlVxQ2x0dFBmZnBGWEEiLCJ1c2VySWQiOjEsInVzZXJSb2xlIjoiVVNFUiIsInRva2VuVHlwZSI6ImFjY2VzcyIsImlhdCI6MTYyMDkyNDc5MCwiZXhwIjoxNjIwOTI1NjkwfQ.76KI4T-lg8lNfBXJgvdx4xNGULuM_njt3vghJDVSI1g";

        given(jwtTokenUtil.generateToken(anyString())).willReturn(token);

        mockMvc.perform(post("/auth/signin/local")
                        .with(csrf())
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(userCache).removeUserFromCache(anyString());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtil).generateToken(anyString());
    }

    @Test
    void testSignInGoogle() throws Exception {
        mockMvc.perform(get("/auth/signin/google")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/api/oauth2/authorization/google"));
    }

    @Test
    void testSignUp() throws Exception {
        AuthRequest authRequest = getObjectFromJson("json/auth_request.json", AuthRequest.class);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJFQUF5bURwWFhUU0FCQUxyejd1dk5VUzV2d0lqY0JUVDFBNzRTOG1aQXU2T2RhY0t2ckMxRzVLSmQ3Z0pGWkNkWkM4ejBqeXY0dEFrbUZoY2FpTDVUM1RLRHRiWkN4VFpCQnZnZ0dQbm9DSG5aQUt0dUtNdldPTXg3eVVXNnpETjZDZHFBM05VTDVrMnlQaGxvR0cxcEdTTGsxenZ1c0xvN0E1WkNLeDNRZjE1OW1Gd0NLQ3MwTGNTcThDVlVxQ2x0dFBmZnBGWEEiLCJ1c2VySWQiOjEsInVzZXJSb2xlIjoiVVNFUiIsInRva2VuVHlwZSI6ImFjY2VzcyIsImlhdCI6MTYyMDkyNDc5MCwiZXhwIjoxNjIwOTI1NjkwfQ.76KI4T-lg8lNfBXJgvdx4xNGULuM_njt3vghJDVSI1g";

        given(jwtTokenUtil.generateToken(anyString())).willReturn(token);

        mockMvc.perform(post("/auth/signup")
                        .with(csrf())
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(userService).createUserAndSendEmail(anyString(), anyString());
        verify(jwtTokenUtil).generateToken(anyString());
    }

    @Test
    void testVerifyEmail() throws Exception {
        VerifyEmailRequest verifyEmailRequest = getObjectFromJson("json/verify_email_request.json", VerifyEmailRequest.class);

        mockMvc.perform(put("/auth/verify-email")
                        .with(csrf())
                        .content(asJsonString(verifyEmailRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).updateEmailVerification(anyString());
    }

    @Test
    void testUpdatePassword() throws Exception {
        UpdatePasswordRequest updatePasswordRequest = getObjectFromJson("json/update_password_request.json", UpdatePasswordRequest.class);

        mockMvc.perform(put("/auth/password-update")
                        .with(csrf())
                        .content(asJsonString(updatePasswordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).updateUserPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testSendEmailForResetPassword() throws Exception {
        SendEmailRequest sendEmailRequest = getObjectFromJson("json/send_email_request.json", SendEmailRequest.class);
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.getUserByEmail(sendEmailRequest.getEmail())).willReturn(user);

        mockMvc.perform(post("/auth/send-password-reset-email")
                        .with(csrf())
                        .content(asJsonString(sendEmailRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getUserByEmail(anyString());
        verify(userService).updateUserPasswordResetTokenAndSendEmail(anyString());
    }

    @Test
    void testSendEmailForResetPasswordException() throws Exception {
        SendEmailRequest sendEmailRequest = getObjectFromJson("json/send_email_request.json", SendEmailRequest.class);

        given(userService.getUserByEmail(sendEmailRequest.getEmail())).willReturn(null);
        given(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MSG_USER_BY_EMAIL_NOT_FOUND,
                new Object[]{sendEmailRequest.getEmail()})).willReturn(sendEmailRequest.getEmail());

        mockMvc.perform(post("/auth/send-password-reset-email")
                        .with(csrf())
                        .content(asJsonString(sendEmailRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect((content().string(sendEmailRequest.getEmail())));

        verify(userService).getUserByEmail(anyString());
        verify(messageCodeUtil).getFullErrorMessageByBundleCode(Constants.ERROR_MSG_USER_BY_EMAIL_NOT_FOUND, new Object[]{sendEmailRequest.getEmail()});
    }

    @Test
    void testResetPassword() throws Exception {
        ResetPasswordRequest resetPasswordRequest = getObjectFromJson("json/reset_password_request.json", ResetPasswordRequest.class);
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.updateUserPasswordByPasswordResetToken(anyString(), anyString())).willReturn(user);

        mockMvc.perform(put("/auth/password-reset")
                        .with(csrf())
                        .content(asJsonString(resetPasswordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.role", is(user.getRole().getAuthority())))
                .andExpect(jsonPath("$.disabled", is(user.getDisabled())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.emailVerified", is(user.getEmailVerified())))
                .andExpect(jsonPath("$.emailVerificationToken", is(user.getEmailVerificationToken())))
                .andExpect(jsonPath("$.emailVerificationTokenExpiresAt", is(user.getEmailVerificationTokenExpiresAt())))
                .andExpect(jsonPath("$.passwordResetToken", is(user.getPasswordResetToken())))
                .andExpect(jsonPath("$.passwordResetTokenExpiresAt", is(user.getPasswordResetTokenExpiresAt())))
                .andExpect(jsonPath("$.provider", is(user.getProvider())))
                .andExpect(jsonPath("$.importHash", is(user.getImportHash())))
                .andExpect(jsonPath("$.wishlist", hasSize(user.getProducts().size())))
                .andExpect(jsonPath("$.avatar", hasSize(user.getFiles().size())));

        verify(userService).updateUserPasswordByPasswordResetToken(anyString(), anyString());
    }

}
