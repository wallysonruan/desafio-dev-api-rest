package com.example.dock.controllers.dtos;

import com.example.dock.models.Portador;
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
public class TransacaoRespostaDto {
    public UUID uuid;
    public LocalDateTime dateTime;
    public TransacaoTipo transacaoTipo;
    public Portador portador;
    public BigDecimal totalDaTransacao;
}
