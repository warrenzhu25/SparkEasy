package com.warren.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warren.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
