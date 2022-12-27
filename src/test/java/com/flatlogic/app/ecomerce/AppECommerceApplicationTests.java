package com.flatlogic.app.ecomerce;

import com.flatlogic.app.ecomerce.controller.UserController;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppECommerceApplicationTests {
    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(userController);
        assertNotNull(userService);
        assertNotNull(userRepository);
    }

}
