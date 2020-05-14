package com.tjf.sample.github.apijpa.specification.model;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

public class DroidSpecification {

	public static Specification<Droid> nameEq(String name) {
		return (root, query, cb) -> cb.equal(root.get("name"), name);
	}

	public static Specification<Droid> functionLike(String function) {
		return (root, query, cb) -> cb.like(cb.upper(root.get("function")), likeConstructor(function));
	}

	private static String likeConstructor(String term) {
		return new StringBuilder().append('%').append(term.trim().toUpperCase()).append('%').toString();
	}

	public static Specification<Droid> heightBetween(double from, double util) {
		return (root, query, cb) -> cb.between(root.get("height"), from, util);
	}

	public static Specification<Droid> droidExists(double height) {
		return (root, query, cb) -> {
			Subquery<Droid> subQuery = query.subquery(Droid.class);
			Root<Droid> subRoot = subQuery.from(Droid.class);
			return cb.exists(subQuery.select(subRoot).where(cb.le(subRoot.get("height"), height),
					cb.equal(subRoot.get("id"), root.get("id"))));
		};
	}

}
