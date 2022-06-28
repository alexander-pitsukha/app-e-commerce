package com.flatlogic.app.generator.configuration;

import com.flatlogic.app.generator.jwt.JwtAuthenticationFilter;
import com.flatlogic.app.generator.oauth2.GoogleOAuth2AuthenticationSuccessHandler;
import com.flatlogic.app.generator.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class AppWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier(Constants.GOOGLE_OIDC_USER_SERVICE)
    private OidcUserService googleOidcUserService;

    @Autowired
    private GoogleOAuth2AuthenticationSuccessHandler googleOAuth2AuthenticationSuccessHandler;

    @Autowired
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private HttpFirewall httpFirewall;

    @Override
    public void init(WebSecurity web) throws Exception {
        super.init(web);
        web.httpFirewall(httpFirewall);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(withDefaults()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable().httpBasic().disable().formLogin().disable().authorizeRequests()
                .antMatchers("/auth/**", "/file/download").permitAll()
                .antMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-resources/**",
                        "/swagger-ui/**").permitAll()
                .anyRequest().authenticated().and().exceptionHandling()
                .and().logout().permitAll()
                .and().oauth2Login().redirectionEndpoint().baseUri("/auth/signin/google/callback")
                .and().userInfoEndpoint().oidcUserService(googleOidcUserService)
                .and().authorizationEndpoint().baseUri("/oauth2/authorize")
                .authorizationRequestRepository(authorizationRequestRepository)
                .and().successHandler(googleOAuth2AuthenticationSuccessHandler);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
