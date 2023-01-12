package com.example.dock.controllers;

import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.controllers.dtos.AgenciaRespostaDto;
import com.example.dock.models.Agencia;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-12T00:43:38-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class AgenciaMapperImpl implements AgenciaMapper {

    @Override
    public Agencia agenciaComandoCriarDtoToAgencia(AgenciaComandoCriarDto agenciaComandoCriarDto) {
        if ( agenciaComandoCriarDto == null ) {
            return null;
        }

        Agencia agencia = new Agencia();

        agencia.nome = agenciaComandoCriarDto.nome;

        return agencia;
    }

    @Override
    public AgenciaRespostaDto agenciaToAgenciaRespostaDto(Agencia agencia) {
        if ( agencia == null ) {
            return null;
        }

        AgenciaRespostaDto agenciaRespostaDto = new AgenciaRespostaDto();

        agenciaRespostaDto.id = agencia.id;
        agenciaRespostaDto.nome = agencia.nome;

        return agenciaRespostaDto;
    }
}
