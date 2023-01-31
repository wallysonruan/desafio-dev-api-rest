package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.models.Portador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortadorMapper {
    @Mapping(target = "uuid", ignore = true)
    Portador portadorComandoCriarDtoToPortador(PortadorComandoCriarDto portadorComandoCriarDto);

    PortadorRespostaDto portadorToPortadorRespostaDto(Portador portador);
}