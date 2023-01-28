package com.example.dock.repositories;

import com.example.dock.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByConta_Uuid(UUID uuid);
    @Query("select (count(t) > 0) from transacao t where t.conta.uuid = ?1")
    boolean existsByConta_Uuid(UUID contaUuid);
}
