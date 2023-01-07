package com.example.dock.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortadorRespostaDto {
    public UUID uuid;
    public String cpf;
    public String nome_completo;
}
