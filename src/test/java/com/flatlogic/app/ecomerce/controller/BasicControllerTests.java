package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.configuration.HttpConfiguration;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.jwt.JwtTokenUtil;
import com.flatlogic.app.ecomerce.oauth2.GoogleOAuth2AuthenticationSuccessHandler;
import com.flatlogic.app.ecomerce.oauth2.GoogleOidcUserServiceImpl;
import com.flatlogic.app.ecomerce.security.UserDetailsImpl;
import com.flatlogic.app.ecomerce.util.Constants;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@MockBean(classes = {JpaMetamodelMappingContext.class, PasswordEncoder.class,
        GoogleOAuth2AuthenticationSuccessHandler.class, AuthorizationRequestRepository.class})
@Import(HttpConfiguration.class)
abstract class BasicControllerTests extends AbstractTests {

    @MockBean(name = Constants.GOOGLE_OIDC_USER_SERVICE)
    private GoogleOidcUserServiceImpl googleOidcUserService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    void setUp() throws Exception {
        User user = getObjectFromJson("json/user_1.json", User.class);
        given(jwtTokenUtil.extractUsername(anyString())).willReturn("admin@flatlogic.com");
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
