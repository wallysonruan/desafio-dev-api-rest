package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.models.Portador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortadorMapper {
    Portador portadorComandoCriarDtoToPortador(PortadorComandoCriarDto portadorComandoCriarDto);

    PortadorRespostaDto portadorToPortadorRespostaDto(Portador portador);
}