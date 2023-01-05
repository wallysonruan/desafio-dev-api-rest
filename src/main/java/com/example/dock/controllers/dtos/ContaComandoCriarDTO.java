package com.example.dock.controllers.dtos;

import com.example.dock.models.Agencia;
import com.example.dock.models.Portador;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaComandoCriarDTO {
    @NotNull(message = "O campo PORTADOR não pode ser vazio.")
    @NotBlank(message = "O campo PORTADOR não pode ser vazio.")
    public Portador portador;
    @NotNull(message = "O campo SALDO não pode ser vazio.")
    @NotBlank(message = "O campo SALDO não pode ser vazio.")
    @Min(value = 0, message = "O valor mínimo do saldo é R$0,00.00")
    public BigDecimal saldo;
    @NotNull(message = "O campo AGENCIA não pode ser vazio.")
    @NotBlank(message = "O campo AGENCIA não pode ser vazio.")
    public Agencia agencia;
}