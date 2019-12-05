package br.com.star.wars.model;

import org.springframework.data.jpa.domain.Specification;

public class DroidSpecification {

	public static Specification<Droid> nameLike(String name) {
		return (root, query, cb) -> cb.like(cb.upper(root.get("name")), DroidSpecification.likeConstructor(name));
	}

	public static Specification<Droid> descriptionLike(String description) {
		return (root, query, cb) -> cb.like(cb.upper(root.get("description")),
				DroidSpecification.likeConstructor(description));
	}

	private static String likeConstructor(String term) {
		return new StringBuilder().append('%').append(term.trim().toUpperCase()).append('%').toString();
	}

}
