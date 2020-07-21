package com.tjf.sample.github.apijpa.filter.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

@Repository
@Transactional
public interface AccountModelRepository extends JpaRepository<AccountModel, Integer>, ApiJpaRepository<AccountModel> {
}
