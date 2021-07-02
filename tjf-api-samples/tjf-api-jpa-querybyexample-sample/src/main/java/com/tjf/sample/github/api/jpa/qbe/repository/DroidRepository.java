package com.tjf.sample.github.api.jpa.qbe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.api.jpa.qbe.model.Droid;
import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

@Repository
@Transactional
public interface DroidRepository extends JpaRepository<Droid, Integer>, ApiJpaRepository<Droid> {
}