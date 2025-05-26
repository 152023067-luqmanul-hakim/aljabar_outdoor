package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Custom query methods if needed
}
