package com.flatlogic.app.ecomerce.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.security.AuditorAwareImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy
@EnableJpaAuditing
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${backend.host}")
    private String backendHost;

    @Value("${frontend.host}")
    private String frontendHost;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(backendHost, frontendHost)
                .allowedHeaders(CorsConfiguration.ALL).allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.DELETE.name()).maxAge(1800L);
    }

    /**
     * Create DefaultConversionService bean.
     *
     * @param converters List of Converter
     * @return DefaultConversionService
     */
    @Bean
    @Autowired
    public DefaultConversionService defaultConversionService(List<Converter<?, ?>> converters) {
        var defaultConversionService = new DefaultConversionService();
        converters.forEach(defaultConversionService::addConverter);
        return defaultConversionService;
    }

    /**
     * Create UserCache bean.
     *
     * @return UserCache
     */
    @Bean
    public UserCache userCache() {
        return new SpringCacheBasedUserCache(new ConcurrentMapCache(UserDetails.class.getName()));
    }

    /**
     * Create AuditorAware bean.
     *
     * @return UserCache
     */
    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl();
    }

    /**
     * Set time zone for ObjectMapper.
     *
     * @param objectMapper ObjectMapper
     */
    @Autowired
    public void setTimeZone(final ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

}
