package com.warren.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warren.backend.data.entity.SparkApp;

public interface SparkAppRepository extends JpaRepository<SparkApp, Long> {

	Page<SparkApp> findBy(Pageable page);

	Page<SparkApp> findByNameLikeIgnoreCase(String name, Pageable page);

	int countByNameLikeIgnoreCase(String name);

}
