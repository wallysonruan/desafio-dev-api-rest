package com.example.dock.controllers;

import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.controllers.dtos.AgenciaRespostaDto;
import com.example.dock.models.Agencia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AgenciaMapperImplTest {

    AgenciaMapperImpl agenciaMapper;

    @BeforeEach
    void setUp(){
        agenciaMapper = new AgenciaMapperImpl();
    }

    private final Agencia AGENCIA = Agencia.builder()
            .id(1L)
            .nome("teste")
            .build();

    private final AgenciaComandoCriarDto AGENCIA_COMANDO_CRIAR_DTO = AgenciaComandoCriarDto.builder()
            .nome(AGENCIA.getNome())
            .build();

    @Test
    void agenciaComandoCriarDtoToAgencia_quandoReceberAgenciaComandoCriarDto__deveriaRetornarAgencia(){

        var convertido = agenciaMapper.agenciaComandoCriarDtoToAgencia(AGENCIA_COMANDO_CRIAR_DTO);

        Assertions.assertEquals(Agencia.class, convertido.getClass());
        Assertions.assertNotNull(convertido.getNome());
        Assertions.assertEquals(AGENCIA.getNome(), convertido.getNome());
    }

    @Test
    void agenciaToAgenciaRespostaDto_quandoReceberAgencia__deveriaRetornarAgenciaRespostaDto(){

        var convertido = agenciaMapper.agenciaToAgenciaRespostaDto(AGENCIA);

        Assertions.assertEquals(AgenciaRespostaDto.class, convertido.getClass());
        Assertions.assertNotNull(convertido.getId());
        Assertions.assertNotNull(convertido.getNome());
        Assertions.assertEquals(AGENCIA.getId(), convertido.getId());
        Assertions.assertEquals(AGENCIA.getNome(), convertido.getNome());
    }
}
