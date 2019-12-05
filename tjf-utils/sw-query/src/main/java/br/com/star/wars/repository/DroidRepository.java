package br.com.star.wars.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.star.wars.model.Droid;

@Repository
@Transactional
public interface DroidRepository extends JpaRepository<Droid, Integer>, JpaSpecificationExecutor<Droid> {

	@Query("SELECT d FROM droid d WHERE d.name LIKE CONCAT('%', :name, '%')")
	List<Droid> getByName(@Param("name") String name);

	@Query("SELECT d FROM droid d WHERE d.description LIKE CONCAT('%', :description, '%')")
	List<Droid> getByDescription(@Param("description") String description);

	@Query("SELECT d FROM droid d WHERE d.name LIKE CONCAT('%', :name, '%') AND d.description LIKE CONCAT('%', :description, '%')")
	List<Droid> getByNameAndDescription(@Param("name") String name, @Param("description") String description);

}
