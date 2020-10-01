package com.tarasiuk.project.hotel.repository;

import com.tarasiuk.project.hotel.model.ClientPassport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPassportRepository extends JpaRepository<ClientPassport, Long> {

    Optional<ClientPassport> getClientPassportByAccount_Id(Long id);
}
