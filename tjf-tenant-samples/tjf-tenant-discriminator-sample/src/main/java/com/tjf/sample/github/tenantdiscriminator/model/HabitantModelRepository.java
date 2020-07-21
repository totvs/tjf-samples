package com.tjf.sample.github.tenantdiscriminator.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitantModelRepository extends JpaRepository<HabitantModel, HabitantModelId> {
}
