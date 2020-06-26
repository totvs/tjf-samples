package com.tjf.sample.github.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjf.sample.github.model.Product;

@Repository
@Transactional()
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
