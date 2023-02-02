package com.example.dock.repositories;

import com.example.dock.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContaRepository extends JpaRepository<Conta, UUID> {
    boolean existsByPortador_Uuid(UUID uuid);
}
