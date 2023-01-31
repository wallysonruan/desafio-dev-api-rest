package com.example.dock.controllers;

import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.controllers.dtos.AgenciaRespostaDto;
import com.example.dock.models.Agencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AgenciaMapper {
    @Mapping(target = "id", ignore = true)
    Agencia agenciaComandoCriarDtoToAgencia(AgenciaComandoCriarDto agenciaComandoCriarDto);

    AgenciaRespostaDto agenciaToAgenciaRespostaDto(Agencia agencia);
}
