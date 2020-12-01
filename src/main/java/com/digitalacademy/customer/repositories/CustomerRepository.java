package com.digitalacademy.customer.repositories;

import com.digitalacademy.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findAllById(Long id);
}
