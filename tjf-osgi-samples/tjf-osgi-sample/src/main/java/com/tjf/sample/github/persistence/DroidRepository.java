package com.tjf.sample.github.persistence;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DroidRepository extends CrudRepository<Droid, Integer> {

	@Query("SELECT d FROM droid d WHERE d.name LIKE CONCAT('%', :name, '%')")
	List<Droid> getByName(@Param("name") String name);

	@Query("SELECT d FROM droid d WHERE d.description LIKE CONCAT('%', :description, '%')")
	List<Droid> getByDescription(@Param("description") String description);

	@Query("SELECT d FROM droid d WHERE d.name LIKE CONCAT('%', :name, '%') AND d.description LIKE CONCAT('%', :description, '%')")
	List<Droid> getByNameAndDescription(@Param("name") String name, @Param("description") String description);

}