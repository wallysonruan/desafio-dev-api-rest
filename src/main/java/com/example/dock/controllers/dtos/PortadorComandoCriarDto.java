package com.example.dock.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortadorComandoCriarDto {
    @NotNull(message = "O campo CPF não pode ser vazio.")
    @NotBlank(message = "O campo CPF não pode ser vazio.")
    @CPF
    private String cpf;
    @NotNull(message = "O campo NOME COMPLETO não pode ser vazio.")
    @NotBlank(message = "O campo NOME COMPLETO não pode ser vazio.")
    private String nome_completo;
}
