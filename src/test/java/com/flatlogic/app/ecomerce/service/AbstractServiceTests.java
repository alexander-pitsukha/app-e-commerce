package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.configuration.TestAuditingConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({
        @Sql(scripts = "classpath:sql/import_data.sql"),
        @Sql(scripts = "classpath:sql/delete_all_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
@Import(TestAuditingConfiguration.class)
abstract class AbstractServiceTests extends AbstractTests {
}
