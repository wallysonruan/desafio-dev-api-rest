package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.controllers.dtos.PortadorRespostaDto;
import com.example.dock.models.Portador;
import com.example.dock.services.PortadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PortadorControllerTest {

    PortadorController controller;
    MockMvc mvc;
    ObjectMapper objectMapper;
    @Mock
    PortadorService service;
    @Mock
    PortadorMapper mapper;

    @BeforeEach
    void setUp() {
        controller = new PortadorController(service, mapper);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    private final String URL_DEFAULT = "/portadores";
    private final String CPF = "18241327005";
    private final String NOME_COMPLETO = "Oliver Manoel Anthony Novaes";
    private final PortadorComandoCriarDto PORTADOR_COMANDO_CRIAR = PortadorComandoCriarDto.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();
    private final Portador PORTADOR = Portador.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();
    private final PortadorRespostaDto PORTADOR_RESPOSTA_DTO = PortadorRespostaDto.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();

    @Test
    void criarPortador_deveriaEstarDisponivelNaUrlPadrao() throws Exception{
        String portador_comando_criar_dto_as_json = objectMapper.writeValueAsString(PORTADOR_COMANDO_CRIAR);
        System.out.println(portador_comando_criar_dto_as_json);

        mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portador_comando_criar_dto_as_json)
        ).andExpect(status().isOk());
    }

    @Test
    void criarPortador_quandoReceberUmPortadorComandoCriarValido__deveriaRetornarPortadorSalvoEHttpOk() throws Exception{
        when(service.criarPortador(PORTADOR)).thenReturn(PORTADOR);
        when(mapper.portadorComandoCriarDtoToPortador(PORTADOR_COMANDO_CRIAR)).thenReturn(PORTADOR);
        when(mapper.portadorToPortadorRespostaDto(PORTADOR)).thenReturn(PORTADOR_RESPOSTA_DTO);
        String portador_as_json = objectMapper.writeValueAsString(PORTADOR_COMANDO_CRIAR);

        var response = mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portador_as_json)
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        PortadorRespostaDto json_as_portador_resposta_dto =  objectMapper.readValue(response.getContentAsString(), PortadorRespostaDto.class);

        Assertions.assertEquals(PortadorRespostaDto.class, json_as_portador_resposta_dto.getClass());
    }

}