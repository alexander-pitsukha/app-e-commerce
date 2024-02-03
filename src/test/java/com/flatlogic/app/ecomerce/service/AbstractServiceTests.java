package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AbstractTests;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({
        @Sql(scripts = "classpath:sql/import_data.sql"),
        @Sql(scripts = "classpath:sql/delete_all_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
abstract class AbstractServiceTests extends AbstractTests {
}
