
package com.example.dock.controllers.dtos;

import com.example.dock.models.TransacaoTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoComandoCriarDto {
    public TransacaoTipo transacaoTipo;
    public UUID portador;
    public BigDecimal totalDaTransacao;
}
