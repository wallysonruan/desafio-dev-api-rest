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
    public UUID uuid;
    @Column(name = "date_time")
    public LocalDateTime dateTime;
    @Column(name = "transacao_tipo")
    public TransacaoTipo transacaoTipo;
    @Column(name = "total_da_transacao")
    public BigDecimal totalDaTransacao;
    public BigDecimal saldo;
    @ManyToOne
    @JoinColumn(name = "conta_uuid", referencedColumnName = "uuid")
    public Conta conta;
}