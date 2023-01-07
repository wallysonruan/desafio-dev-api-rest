package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.models.Portador;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface PortadorMapper {
    Portador portadorComandoCriarDtoToPortador(PortadorComandoCriarDto portadorComandoCriarDto);

    PortadorRespostaDto portadorToPortadorRespostaDto(Portador response);
}
