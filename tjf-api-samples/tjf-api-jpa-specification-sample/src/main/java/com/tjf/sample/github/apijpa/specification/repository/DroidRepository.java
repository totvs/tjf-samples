package com.tjf.sample.github.apijpa.specification.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.apijpa.specification.model.Droid;

@Repository
@Transactional
public interface DroidRepository extends JpaRepository<Droid, Integer>{
	Slice<Droid> findAll(Specification<Droid> specification, Pageable pageable);
}