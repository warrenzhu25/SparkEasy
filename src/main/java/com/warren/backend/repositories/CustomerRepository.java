package com.warren.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warren.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
