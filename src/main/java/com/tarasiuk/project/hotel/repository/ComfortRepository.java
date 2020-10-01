package com.tarasiuk.project.hotel.repository;

import com.tarasiuk.project.hotel.model.Comfort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComfortRepository extends JpaRepository<Comfort, Long> {

    Comfort findByName(String name);
}
