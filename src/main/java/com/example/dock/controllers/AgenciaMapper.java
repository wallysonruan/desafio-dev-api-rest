package com.example.dock.controllers;

import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.controllers.dtos.AgenciaRespostaDto;
import com.example.dock.models.Agencia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgenciaMapper {
    Agencia agenciaComandoCriarDtoToAgencia(AgenciaComandoCriarDto agenciaComandoCriarDto);

    AgenciaRespostaDto agenciaToAgenciaRespostaDto(Agencia agencia);
}
