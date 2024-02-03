package com.flatlogic.app.ecomerce.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(FileController.class)
@WithMockUser
class FileControllerTests extends BasicControllerTests {
}
