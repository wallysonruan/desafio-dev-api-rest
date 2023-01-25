package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.models.Transacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {
}