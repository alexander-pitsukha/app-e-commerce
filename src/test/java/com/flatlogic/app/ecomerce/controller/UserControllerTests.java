package com.flatlogic.app.ecomerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.controller.request.UserRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetUsers() throws Exception {
        List<User> users = getObjectFromJson("json/users.json", new TypeReference<>() {
        });

        given(userService.getUsers(anyInt(), anyInt(), anyString())).willReturn(users);

        mockMvc.perform(get("/users")
                        .param("filter", "")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("orderBy", "")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows.*", hasSize(users.size())))
                .andExpect(jsonPath("$.count", is(users.size())));

        verify(userService).getUsers(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetUsersAutocompletes() throws Exception {
        List<User> users = getObjectFromJson("json/users_autocomplete.json", new TypeReference<>() {
        });

        given(userService.getUsers(anyString(), anyInt())).willReturn(users);

        mockMvc.perform(get("/users/autocomplete")
                        .param("query", "a")
                        .param("limit", "10")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(users.size())));

        verify(userService).getUsers(anyString(), anyInt());
    }

    @Test
    void testGetUserById() throws Exception {
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.getUserById(any(UUID.class))).willReturn(user);

        mockMvc.perform(get("/users/{id}", user.getId())
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

        verify(userService).getUserById(any(UUID.class));
    }

    @Test
    void testSaveUser() throws Exception {
        RequestData<UserRequest> requestData = getObjectFromJson("json/user_request_1.json", new TypeReference<>() {
        });
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.saveUser(any(UserRequest.class))).willReturn(user);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/users/*"))
                .andExpect(jsonPath("$.id").isNotEmpty())
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

        verify(userService).saveUser(any(UserRequest.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        RequestData<UserRequest> requestData = getObjectFromJson("json/user_request_1.json", new TypeReference<>() {
        });
        User user = getObjectFromJson("json/user_1.json", User.class);

        given(userService.updateUser(any(UUID.class), any(UserRequest.class))).willReturn(user);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
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

        verify(userService).updateUser(any(UUID.class), any(UserRequest.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        willDoNothing().given(userService).deleteUser(any(UUID.class));

        mockMvc.perform(delete("/users/{id}", UUID.randomUUID())
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(any(UUID.class));
    }

}
