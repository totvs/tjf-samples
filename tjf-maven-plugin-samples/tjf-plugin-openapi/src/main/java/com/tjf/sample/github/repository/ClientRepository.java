package com.tjf.sample.github.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.model.Client;

@Repository
@Transactional()
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
