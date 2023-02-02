package com.example.dock.controllers;

import com.example.dock.controllers.dtos.TransacaoRespostaDto;
import com.example.dock.models.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {
    @Mapping(target = "conta.portador", ignore = true)
    @Mapping(target = "conta.agencia.nome", ignore = true)
    TransacaoRespostaDto transacaoToTransacaoDto(Transacao transacao);
}