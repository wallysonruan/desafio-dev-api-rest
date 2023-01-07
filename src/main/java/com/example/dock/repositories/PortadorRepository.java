package com.example.dock.repositories;

import com.example.dock.models.Portador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortadorRepository extends JpaRepository<Portador, String> {
}
