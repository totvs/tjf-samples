package com.tjf.sample.github.multidb.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.multidb.entity.Person;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
