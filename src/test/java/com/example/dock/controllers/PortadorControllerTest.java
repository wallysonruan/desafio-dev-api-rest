package com.example.dock.controllers;

import com.example.dock.Notification;
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

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    Notification notification;

    @BeforeEach
    void setUp() {
        controller = new PortadorController(service, mapper);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        notification = new Notification();
    }

    private final String URL_DEFAULT = "/portadores";
    private final UUID UUID_DEFAULT = UUID.randomUUID();
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
            .uuid(UUID_DEFAULT)
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();

    @Test
    void criarPortador_quandoReceberUmPortadorComandoCriarValido__deveriaRetornarPortadorSalvoEHttpOk() throws Exception{
        notification.setResultado(PORTADOR);

        when(service.criarPortador(PORTADOR)).thenReturn(notification);
        when(mapper.portadorComandoCriarDtoToPortador(PORTADOR_COMANDO_CRIAR)).thenReturn(PORTADOR);
        when(mapper.portadorToPortadorRespostaDto(PORTADOR)).thenReturn(PORTADOR_RESPOSTA_DTO);
        String portador_comando_criar_as_json = objectMapper.writeValueAsString(PORTADOR_COMANDO_CRIAR);

        var response = mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portador_comando_criar_as_json)
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        PortadorRespostaDto json_as_portador_resposta_dto =  objectMapper.readValue(response.getContentAsString(), PortadorRespostaDto.class);

        Assertions.assertEquals(PortadorRespostaDto.class, json_as_portador_resposta_dto.getClass());
        Assertions.assertNotNull(json_as_portador_resposta_dto.getUuid());
        Assertions.assertNotNull(json_as_portador_resposta_dto.getCpf());
        Assertions.assertNotNull(json_as_portador_resposta_dto.getNome_completo());
    }

    @Test
    void deletarPortador_quandoReceberUuidDePortadorRegistrado__ApagarERetornar204() throws Exception{
        when(service.deletarPortador(UUID_DEFAULT)).thenReturn(notification);

        mvc.perform(
                delete(URL_DEFAULT + "/" + UUID_DEFAULT)
        ).andExpect(status().isNoContent());
    }

    @Test
    void deletarPortador_quandoReceberUuidDePortadorNãoRegistrado__Retornar404() throws Exception{
        var mensagem_de_erro = "Portador (a) não cadastrado (a).";
        notification.addError(mensagem_de_erro);
        when(service.deletarPortador(UUID_DEFAULT)).thenReturn(notification);

        var response = mvc.perform(
                delete(URL_DEFAULT + "/" + UUID_DEFAULT)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();

        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertEquals(mensagem_de_erro, response.getContentAsString());
    }
}