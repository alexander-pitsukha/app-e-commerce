package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.dto.UserDto;
import com.flatlogic.app.ecomerce.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class UserToDtoConverterTests extends AbstractTests {

    @Autowired
    private UserToDtoConverter converter;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public UserToDtoConverter dtoToFormConverter() {
            FileToDtoConverter fileToDtoConverter = new FileToDtoConverter();
            ProductToDtoConverter productToDtoConverter = new ProductToDtoConverter(new CategoryToDtoConverter(), fileToDtoConverter);
            return new UserToDtoConverter(productToDtoConverter, new FileToDtoConverter());
        }
    }

    @Test
    void testConvert() throws IOException {
        User user = getObjectFromJson("json/user_1.json", User.class);

        UserDto userDto = converter.convert(user);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());

        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getPhoneNumber(), userDto.getPhoneNumber());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getRole().getAuthority(), userDto.getRole());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getDisabled(), userDto.getDisabled());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getEmailVerified(), userDto.getEmailVerified());
        assertEquals(user.getEmailVerificationToken(), userDto.getEmailVerificationToken());
        assertEquals(user.getEmailVerificationTokenExpiresAt(), userDto.getEmailVerificationTokenExpiresAt());
        assertEquals(user.getPasswordResetToken(), userDto.getPasswordResetToken());
        assertEquals(user.getProvider(), userDto.getProvider());
        assertEquals(user.getImportHash(), userDto.getImportHash());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
        assertEquals(user.getDeletedAt(), userDto.getDeletedAt());
        assertEquals(user.getCreatedBy().getId(), userDto.getCreatedById());
        assertEquals(user.getUpdatedBy().getId(), userDto.getUpdatedById());
        assertEquals(user.getProducts().size(), userDto.getProductDtos().size());
        assertEquals(user.getFiles().size(), userDto.getFileDtos().size());
    }

}
