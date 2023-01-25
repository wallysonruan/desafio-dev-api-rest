package com.example.dock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transacao")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {
    @Id
    @GeneratedValue(generator = "uuid")
    UUID uuid;
    LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "portador_uuid")
    Portador portador;
    TransacaoTipo transacaoTipo;
    BigDecimal totalDaTransacao;
}