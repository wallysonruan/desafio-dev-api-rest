package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.controllers.dtos.AgenciaRespostaDto;
import com.example.dock.models.Agencia;
import com.example.dock.services.AgenciaService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AgenciaControllerTest {

    AgenciaController controller;
    MockMvc mvc;
    ObjectMapper objectMapper;
    @Mock
    AgenciaService service;
    Notification notification;
    @Mock
    AgenciaMapper mapper;

    private final String URL_DEFAULT = "/agencias";
    private final String AGENCIA_NOME = "BH-CENTRO-MG";
    private final Agencia AGENCIA = Agencia.builder()
            .id(1l)
            .nome(AGENCIA_NOME)
            .build();
    private final AgenciaComandoCriarDto AGENCIA_COMANDO_CRIAR_DTO = AgenciaComandoCriarDto.builder()
            .nome(AGENCIA_NOME)
            .build();

    private final AgenciaRespostaDto AGENCIA_RESPOSTA_DTO = AgenciaRespostaDto.builder()
            .id(1L)
            .nome(AGENCIA_NOME)
            .build();

    @BeforeEach
    void setUp(){
        controller = new AgenciaController(service, mapper);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        notification = new Notification();
    }


    @Test
    void criarAgencia_quandoReceberUmAgenciaComandoCriarDtoValido__deveriaSalvarNoBancoDeDadosRetornarAgenciaRespostaDtoEHttp200() throws Exception{
        notification.setResultado(AGENCIA);
        when(service.criarAgencia(AGENCIA)).thenReturn(notification);
        when(mapper.agenciaComandoCriarDtoToAgencia(AGENCIA_COMANDO_CRIAR_DTO)).thenReturn(AGENCIA);
        when(mapper.agenciaToAgenciaRespostaDto(AGENCIA)).thenReturn(AGENCIA_RESPOSTA_DTO);

        var response = mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AGENCIA_COMANDO_CRIAR_DTO))
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        var response_json_as_object = objectMapper.readValue(response.getContentAsString(), AgenciaRespostaDto.class);

        Assertions.assertEquals(AGENCIA_RESPOSTA_DTO, response_json_as_object);
        Assertions.assertNotNull(AGENCIA_RESPOSTA_DTO.getId());
        Assertions.assertNotNull(AGENCIA_RESPOSTA_DTO.getNome());
    }

    @Test
    void criarAgencia_quandoReceberUmAgenciaComandoCriarVazio__retornar404() throws Exception{
        var response = mvc.perform(
                        post(URL_DEFAULT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("")
                ).andExpect(status().isBadRequest());
    }

    @Test
    void criarAgencia_quandoReceberNomeDeAgenciaJaCadastrado__retornar403ENotificationComErro() throws Exception{
        notification.addError("Agencia já cadastrada.");
        when(service.criarAgencia(any())).thenReturn(notification);

        var response = mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AGENCIA_COMANDO_CRIAR_DTO))
        ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().contains("Agencia já cadastrada."));
    }

    @Test
    void deletarAgencia_quandoReceberIdDeAgenciaRegistrada__deveriaRetornar204() throws Exception{
        when(service.deletarAgencia(AGENCIA.getId())).thenReturn(notification);

        var response = mvc.perform(
                delete(URL_DEFAULT + "/" + AGENCIA.id)
        ).andExpect(status().isNoContent())
                .andReturn().getResponse();

        verify(service, times(1)).deletarAgencia(AGENCIA.getId());
        Assertions.assertEquals("", response.getContentAsString());
    }

    @Test
    void deletarAgencia_quandoReceberIdDeAgenciaNaoRegistrada__deveriaRetornar404() throws Exception{
        String MENSAGEM_DE_ERRO = "Agência não registrada.";
        notification.addError(MENSAGEM_DE_ERRO);
        when(service.deletarAgencia(AGENCIA.getId())).thenReturn(notification);

        var response = mvc.perform(
                        delete(URL_DEFAULT + "/" + AGENCIA.id)
                ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        verify(service, times(1)).deletarAgencia(AGENCIA.getId());
        Assertions.assertEquals(MENSAGEM_DE_ERRO, response.getContentAsString());
    }
}