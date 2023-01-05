package com.example.dock.controllers.dtos;

import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class ContaRespostaDTO {
    @NotNull
    @NotBlank
    @NotNull
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

    public static ContaRespostaDTO contaToContaRespostaDto(Conta conta){
        return ContaRespostaDTO.builder()
                .uuid(conta.uuid)
                .portador(conta.portador)
                .saldo(conta.saldo)
                .agencia(conta.agencia)
                .ativada(true)
                .bloqueada(false)
                .build();
    }
}
