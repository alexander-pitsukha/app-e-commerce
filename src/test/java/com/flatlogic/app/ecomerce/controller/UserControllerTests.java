package com.flatlogic.app.ecomerce.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTests extends BasicControllerTests {
}
