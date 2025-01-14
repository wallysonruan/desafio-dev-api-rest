package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaRespostaDto;
import com.example.dock.models.Conta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContaMapper {
    ContaRespostaDto contaToContaRespostaDto(Conta conta);
}