package com.tjf.sample.github.migration.schema;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MigrationApplication.class)
public class MigrationTests {
	
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
