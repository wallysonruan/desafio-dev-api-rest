package com.example.dock.models;

import lombok.*;

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
    @Setter(AccessLevel.NONE)
    UUID uuid;
    @Column(name = "date_time")
    LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "conta_uuid")
    Conta conta;
    @Column(name = "transacao_tipo")
    TransacaoTipo transacaoTipo;
    @Column(name = "total_da_transacao")
    BigDecimal totalDaTransacao;
}