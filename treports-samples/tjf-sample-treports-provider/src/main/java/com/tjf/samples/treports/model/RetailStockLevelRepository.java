package com.tjf.samples.treports.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

public interface RetailStockLevelRepository extends JpaRepository<RetailStockLevel, String>, ApiJpaRepository<RetailStockLevel>{
}