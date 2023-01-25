package com.example.dock.controllers.dtos;

import com.example.dock.models.Agencia;
import com.example.dock.models.Portador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaRespostaDto {
    @NotNull
    @NotBlank
    public UUID uuid;
    @NotNull
    @NotBlank
    public Portador portador;
    @NotNull
    @NotBlank
    public BigDecimal saldo;
    @NotNull
    @NotBlank
    public Agencia agencia;
    @NotNull
    @NotBlank
    public Boolean ativada;
    @NotNull
    @NotBlank
    public Boolean bloqueada;
}
