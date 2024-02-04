package com.flatlogic.app.ecomerce.repositoty;

import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllByDeletedAtIsNullPage() {
        List<User> users = userRepository.findAllByDeletedAtIsNull(Pageable.ofSize(10));

        assertEquals(3, users.size());
    }

    @Test
    void testFindAllByDeletedAtIsNullSort() {
        List<User> users = userRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());

        assertEquals(3, users.size());
    }

    @Test
    void testFindAllByEmailLikeAndDeletedAtIsNullOrderByEmailAsc() {
        List<User> users = userRepository.findAllByEmailLikeAndDeletedAtIsNullOrderByEmailAsc("admin",
                PageRequest.of(0, 10, Sort.by(Constants.EMAIL).ascending()));

        assertEquals(1, users.size());
    }

    @Test
    void testFindAllByEmailVerificationToken() {
        List<User> users = userRepository.findAllByEmailVerificationToken();

        assertEquals(1, users.size());
    }

    @Test
    void testFindByEmail() {
        String email = "admin@flatlogic.com";

        User user = userRepository.findByEmail(email);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(true, user.getEmailVerified());
    }

    @Test
    void testFindByEmailAndEmailVerifiedIsTrue() {
        String email = "admin@flatlogic.com";

        User user = userRepository.findByEmailAndEmailVerifiedIsTrue(email);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(true, user.getEmailVerified());
    }

    @Test
    void testFindByEmailVerificationToken() {
        String emailVerificationToken = "37F1FE9C2C574A5CB2CB16FE2FC705C4";

        User user = userRepository.findByEmailVerificationToken(emailVerificationToken);

        assertNotNull(user.getId());
        assertEquals(false, user.getEmailVerified());
        assertEquals(emailVerificationToken, user.getEmailVerificationToken());
    }

    @Test
    void testFindByPasswordResetToken() {
        String passwordResetToken = "E04715B3A09B4E0B8D72697B1855EB82";

        User user = userRepository.findByPasswordResetToken(passwordResetToken);

        assertNotNull(user.getId());
        assertEquals(passwordResetToken, user.getPasswordResetToken());
    }

    @Test
    void testUpdateEmailVerificationToken() {
        String emailVerificationToken = "37F1FE9C2C574A5CB2CB16FE2FC705C4";

        userRepository.updateEmailVerificationToken(emailVerificationToken);
        User user = entityManager.find(User.class, UUID.fromString("12740c3d-2788-4401-af6c-b869ec4a4639"));

        assertNotNull(user.getId());
        assertEquals(true, user.getEmailVerified());
        assertNull(user.getEmailVerificationToken());
        assertNull(user.getEmailVerificationTokenExpiresAt());
    }

    @Test
    void testUpdatePasswordResetToken() {
        String passwordResetToken = "51842A42D3DE42E1B27410A999E29FF5";
        String email = "user1@flatlogic.com";

        userRepository.updatePasswordResetToken(passwordResetToken, LocalDateTime.now(), email);
        User user = entityManager.find(User.class, UUID.fromString("12740c3d-2788-4401-af6c-b869ec4a4639"));

        assertNotNull(user.getId());
        assertEquals(passwordResetToken, user.getPasswordResetToken());
        assertNotNull(user.getPasswordResetTokenExpiresAt());
    }

    @Test
    void testUpdateDeletedAt() {
        UUID id = UUID.fromString("12740c3d-2788-4401-af6c-b869ec4a4639");

        userRepository.updateDeletedAt(id, LocalDateTime.now());
        User user = entityManager.find(User.class, id);

        assertNotNull(user.getId());
        assertNotNull(user.getDeletedAt());
    }

}
