package com.example.dock.controllers.dtos;

import com.example.dock.models.Conta;
import com.example.dock.models.TransacaoTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoRespostaPorPeriodoDto {
    public UUID uuid;
    public LocalDateTime dateTime;
    public TransacaoTipo transacaoTipo;
    public BigDecimal totalDaTransacao;
    public BigDecimal saldo;
}
