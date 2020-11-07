package com.warren.backend.repositories;

import com.warren.backend.data.entity.AppRun;
import com.warren.backend.data.entity.SparkApp;
import com.warren.backend.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRunRepository extends JpaRepository<AppRun, Long> {
    Page<AppRun> findBy(Pageable pageable);

    Page<AppRun> findByNameLikeIgnoreCase(String name, Pageable page);

    int countByNameLikeIgnoreCase(String name);
}
