package com.tjf.sample.github.multidb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.multidb.entity.Person;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface JediRepository extends JpaRepository<Person, Integer> {
}
