package br.com.star.wars.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.star.wars.model.Droid;
import br.com.star.wars.model.DroidSpecification;
import br.com.star.wars.model.ResponseData;
import br.com.star.wars.repository.DroidRepository;

@RestController
@RequestMapping("/api/v1/droid")
public class DroidController {

	@Autowired
	private DroidRepository droidRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping(path = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Droid> getAllDroids() {
		return droidRepository.findAll();
	}

	@GetMapping(path = "/namedQuery/getByName/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByName(@PathVariable("name") String name) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByName(name);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Name", totalTime, droids);
	}

	@GetMapping(path = "/namedQuery/getByDescription/{description}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByDescription(@PathVariable("description") String description) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByDescription(description);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Description", totalTime, droids);
	}

	@GetMapping(path = "/namedQuery/getByNameAndDescription/{name}/{description}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByNameAndDescription(@PathVariable("name") String name,
			@PathVariable("description") String description) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByNameAndDescription(name, description);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Name And Description", totalTime, droids);
	}

	@GetMapping(path = "/criteria/getByName/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData criteriaGetByName(@PathVariable("name") String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Droid> criteriaQuery = criteriaBuilder.createQuery(Droid.class);

		Root<Droid> droid = criteriaQuery.from(Droid.class);
		Predicate filterByName = criteriaBuilder.like(droid.get("name"), "%" + name + "%");
		criteriaQuery.where(filterByName);

		TypedQuery<Droid> query = entityManager.createQuery(criteriaQuery);

		long startTime = System.nanoTime();
		List<Droid> droids = query.getResultList();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Criteria By Name", totalTime, droids);
	}

	@GetMapping(path = "/criteria/getByDescription/{description}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData criteriaGetByDescription(@PathVariable("description") String description) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Droid> criteriaQuery = criteriaBuilder.createQuery(Droid.class);

		Root<Droid> droid = criteriaQuery.from(Droid.class);
		Predicate filterByDescription = criteriaBuilder.like(droid.get("description"), "%" + description + "%");
		criteriaQuery.where(filterByDescription);

		TypedQuery<Droid> query = entityManager.createQuery(criteriaQuery);

		long startTime = System.nanoTime();
		List<Droid> droids = query.getResultList();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Criteria By Description", totalTime, droids);
	}

	@GetMapping(path = "/criteria/getByNameAndDescription/{name}/{description}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseData criteriaGetByNameAndDescription(@PathVariable("name") String name,
			@PathVariable("description") String description) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Droid> criteriaQuery = criteriaBuilder.createQuery(Droid.class);

		Root<Droid> droid = criteriaQuery.from(Droid.class);
		Predicate filterByName = criteriaBuilder.like(droid.get("name"), "%" + name + "%");
		Predicate filterByDescription = criteriaBuilder.like(droid.get("description"), "%" + description + "%");
		criteriaQuery.where(filterByName, filterByDescription);

		TypedQuery<Droid> query = entityManager.createQuery(criteriaQuery);

		long startTime = System.nanoTime();
		List<Droid> droids = query.getResultList();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Criteria By Name And Description", totalTime, droids);
	}

	@GetMapping(path = "/specification/getByName/{name}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseData specificationGetByName(@PathVariable("name") String name) {

		Specification<Droid> spec = Specification.where(DroidSpecification.nameLike(name));

		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.findAll(spec);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Specification By Name", totalTime, droids);
	}

	@GetMapping(path = "/specification/getByDescription/{description}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseData specificationGetByDescription(@PathVariable("description") String description) {

		Specification<Droid> spec = Specification.where(DroidSpecification.descriptionLike(description));

		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.findAll(spec);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Specification By Description", totalTime, droids);
	}

	@GetMapping(path = "/specification/getByNameAndDescription/{name}/{description}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseData specificationGetByNameAndDescription(@PathVariable("name") String name,
			@PathVariable("description") String description) {

		Specification<Droid> spec = Specification.where(DroidSpecification.nameLike(name))
				.and((DroidSpecification.descriptionLike(description)));

		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.findAll(spec);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Specification By Name And Description", totalTime, droids);
	}
	
}
