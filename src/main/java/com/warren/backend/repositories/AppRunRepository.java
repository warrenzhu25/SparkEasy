package com.warren.backend.repositories;

import com.warren.backend.data.entity.AppRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRunRepository extends JpaRepository<AppRun, Long> {

}
