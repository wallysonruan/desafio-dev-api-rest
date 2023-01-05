package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.models.Conta;
import org.mapstruct.Mapper;

@Mapper
public interface ContaMapper {
    ContaRespostaDTO contaToContaRespostaDto(Conta conta);
}