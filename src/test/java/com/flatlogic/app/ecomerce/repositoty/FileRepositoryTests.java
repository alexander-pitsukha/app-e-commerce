package com.flatlogic.app.ecomerce.repositoty;

import com.flatlogic.app.ecomerce.entity.File;
import com.flatlogic.app.ecomerce.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FileRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private FileRepository fileRepository;

    @Test
    void testExistsByPrivateUrl() {
        boolean result = fileRepository.existsByPrivateUrl("users/avatar/58ae5c09-a337-41c0-acf2-07073041247a.png");

        assertTrue(result);
    }

    @Test
    void testFindAllByBelongsTo() {
        List<File> files = fileRepository.findAllByBelongsTo("users");

        assertEquals(1, files.size());
    }

}
