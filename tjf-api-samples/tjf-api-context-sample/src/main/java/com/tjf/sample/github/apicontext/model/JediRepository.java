package com.tjf.sample.github.apicontext.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

@Repository
@Transactional
public interface JediRepository extends JpaRepository<Jedi, Integer>, ApiJpaRepository<Jedi> {
}