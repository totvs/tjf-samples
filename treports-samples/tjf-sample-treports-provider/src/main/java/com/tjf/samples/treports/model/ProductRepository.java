package com.tjf.samples.treports.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

public interface ProductRepository extends JpaRepository<Product, String>, ApiJpaRepository<Product>{
}