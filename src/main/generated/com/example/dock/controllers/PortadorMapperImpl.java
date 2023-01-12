package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.models.Portador;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-12T00:43:37-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class PortadorMapperImpl implements PortadorMapper {

    @Override
    public Portador portadorComandoCriarDtoToPortador(PortadorComandoCriarDto portadorComandoCriarDto) {
        if ( portadorComandoCriarDto == null ) {
            return null;
        }

        Portador portador = new Portador();

        portador.setCpf( portadorComandoCriarDto.cpf );
        portador.nome_completo = portadorComandoCriarDto.nome_completo;

        return portador;
    }

    @Override
    public PortadorRespostaDto portadorToPortadorRespostaDto(Portador portador) {
        if ( portador == null ) {
            return null;
        }

        PortadorRespostaDto portadorRespostaDto = new PortadorRespostaDto();

        portadorRespostaDto.uuid = portador.uuid;
        portadorRespostaDto.cpf = portador.cpf;
        portadorRespostaDto.nome_completo = portador.nome_completo;

        return portadorRespostaDto;
    }
}
