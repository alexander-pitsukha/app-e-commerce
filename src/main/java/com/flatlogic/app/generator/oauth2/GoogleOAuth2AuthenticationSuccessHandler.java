package com.flatlogic.app.generator.oauth2;

import com.flatlogic.app.generator.jwt.JwtTokenUtil;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class GoogleOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${frontend.host}")
    private String frontendHost;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get(Constants.EMAIL);
        var user = userRepository.findByEmail(email);
        String token = jwtTokenUtil.generateToken(user.getEmail());
        var redirectionUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/login")
                .queryParam(Constants.TOKEN, token).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
    }

}