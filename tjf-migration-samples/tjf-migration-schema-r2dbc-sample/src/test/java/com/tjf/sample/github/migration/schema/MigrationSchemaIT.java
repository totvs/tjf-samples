package com.tjf.sample.github.migration.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.r2dbc.connection.ConnectionFactoryUtils;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = MigrationSchemaApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MigrationSchemaIT {
	
	@Autowired
	private ConnectionFactory connectionFactory;

	@Test
	public void testR2dbcConnection() {
		
		Mono.from(connectionFactory.create())
		  .flatMapMany(connection -> connection
		    .createStatement("SET SCHEMA _TATOOINE;")
		    .execute())
		  .flatMap(result -> result
		    .map((row, rowMetadata) -> row.get("firstname", String.class)))
		  .doOnNext(System.out::println)
		  .subscribe();
	}
	
	@Test
	public void testSchemas() throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swfamilytree", "postgres", "postgres");

		PreparedStatement statement = connection.prepareStatement("SET search_path TO _TATOOINE;");
		statement.execute();
		assertEquals("_TATOOINE", connection.getSchema());

		PreparedStatement statement2 = connection.prepareStatement("SET search_path TO _ALDERAAN");
		statement2.execute();
		assertEquals("_ALDERAAN", connection.getSchema());

		PreparedStatement statement3 = connection.prepareStatement("SET search_path TO _BESPIN");
		statement3.execute();
		assertEquals("_BESPIN", connection.getSchema());
	}
}
