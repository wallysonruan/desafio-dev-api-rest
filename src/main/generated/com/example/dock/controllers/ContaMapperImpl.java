package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.models.Conta;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-12T00:43:38-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class ContaMapperImpl implements ContaMapper {

    @Override
    public ContaRespostaDTO contaToContaRespostaDto(Conta conta) {
        if ( conta == null ) {
            return null;
        }

        ContaRespostaDTO contaRespostaDTO = new ContaRespostaDTO();

        contaRespostaDTO.uuid = conta.uuid;
        contaRespostaDTO.portador = conta.portador;
        contaRespostaDTO.saldo = conta.saldo;
        contaRespostaDTO.agencia = conta.agencia;
        contaRespostaDTO.ativada = conta.ativada;
        contaRespostaDTO.bloqueada = conta.bloqueada;

        return contaRespostaDTO;
    }
}
