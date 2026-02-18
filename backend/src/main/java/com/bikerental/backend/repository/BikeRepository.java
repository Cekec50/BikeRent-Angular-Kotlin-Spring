package com.bikerental.backend.repository;

import com.bikerental.backend.model.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    List<Bike> findByStatusNot(Integer status);
    Optional<Bike> findByIdAndStatusNot(Long id, Integer status);
}
