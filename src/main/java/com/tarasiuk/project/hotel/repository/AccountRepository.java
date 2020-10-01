package com.tarasiuk.project.hotel.repository;

import com.tarasiuk.project.hotel.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);

    @Modifying
    @Query("update Account a set a.active = 0 where a.id = :id")
    void changeActive(@Param("id") Long account_id);
}
