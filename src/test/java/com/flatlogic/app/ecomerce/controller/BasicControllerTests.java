package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.jwt.JwtTokenUtil;
import com.flatlogic.app.ecomerce.security.UserDetailsImpl;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@MockBean(JpaMetamodelMappingContext.class)
abstract class BasicControllerTests extends AbstractTests {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    void setUp() throws Exception {
        User user = getObjectFromJson("json/user.json", User.class);
        given(jwtTokenUtil.extractUsername(anyString())).willReturn("admin");
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(UserDetailsImpl.builder().user(user).build());
        given(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class))).willReturn(true);
    }

    HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(UUID.randomUUID().toString());
        return httpHeaders;
    }

    String asJsonString(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
