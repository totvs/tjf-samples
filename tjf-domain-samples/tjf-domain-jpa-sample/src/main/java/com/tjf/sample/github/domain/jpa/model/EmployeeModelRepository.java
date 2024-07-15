package com.tjf.sample.github.domain.jpa.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EmployeeModelRepository
		extends JpaRepository<EmployeeModel, Integer>, JpaSpecificationExecutor<EmployeeModel> {
}
