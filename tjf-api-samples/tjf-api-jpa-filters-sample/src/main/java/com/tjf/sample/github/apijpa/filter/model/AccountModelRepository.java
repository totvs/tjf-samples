package com.tjf.sample.github.apijpa.filter.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountModelRepository extends JpaRepository<AccountModel, Integer>, JpaSpecificationExecutor<AccountModel> {
}
