package com.tjf.sample.github.migration.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MigrationSchemaApplication.class)
public class MigrationSchemaIT {

	@Test
	public void testSchemas() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:starwarsdb", "sa", "");

		PreparedStatement statement = connection.prepareStatement("SET SCHEMA _TATOOINE");
		statement.execute();
		assertEquals("_TATOOINE", connection.getSchema());

		PreparedStatement statement2 = connection.prepareStatement("SET SCHEMA _ALDERAAN");
		statement2.execute();
		assertEquals("_ALDERAAN", connection.getSchema());

		PreparedStatement statement3 = connection.prepareStatement("SET SCHEMA _BESPIN");
		statement3.execute();
		assertEquals("_BESPIN", connection.getSchema());
	}
}
