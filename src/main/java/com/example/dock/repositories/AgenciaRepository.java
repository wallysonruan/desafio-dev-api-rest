package com.example.dock.repositories;

import com.example.dock.models.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
    Boolean existsByNome(String nome);
}
