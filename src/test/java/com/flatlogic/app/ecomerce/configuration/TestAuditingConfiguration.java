package com.flatlogic.app.ecomerce.configuration;

import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.security.AuditorAwareImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Optional;
import java.util.UUID;

@TestConfiguration
public class TestAuditingConfiguration {

    @Bean
    @Primary
    public AuditorAwareImpl auditorProvider() {
        return new TestAuditorAware();
    }

    public static class TestAuditorAware extends AuditorAwareImpl {

        @Override
        public Optional<User> getCurrentAuditor() {
            User user = new User();
            user.setId(UUID.fromString("52bc1fb6-0fe5-4647-8cbc-8c55e156b889"));
            return Optional.of(user);
        }
    }
}