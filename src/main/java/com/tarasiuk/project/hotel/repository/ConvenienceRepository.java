package com.tarasiuk.project.hotel.repository;

import com.tarasiuk.project.hotel.model.Convenience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, Long> {

    Optional<Convenience> findByName(String name);
}
