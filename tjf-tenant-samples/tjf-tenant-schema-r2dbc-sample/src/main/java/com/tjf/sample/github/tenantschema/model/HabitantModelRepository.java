package com.tjf.sample.github.tenantschema.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitantModelRepository extends JpaRepository<HabitantModel, String> {
}
