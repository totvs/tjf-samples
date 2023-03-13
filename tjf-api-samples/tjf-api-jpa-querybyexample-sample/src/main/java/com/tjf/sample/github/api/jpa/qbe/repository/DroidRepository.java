package com.tjf.sample.github.api.jpa.qbe.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.api.jpa.qbe.model.Droid;

@Repository
@Transactional
public interface DroidRepository extends JpaRepository<Droid, Integer> {
}