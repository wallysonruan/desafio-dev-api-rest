package com.example.dock.controllers;

import com.example.dock.models.Portador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PortadorControllerTest {

    PortadorController controller;
    MockMvc mvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        controller = new PortadorController();
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    private final String URL_DEFAULT = "/portadores";
    private final String CPF = "18241327005";
    private final String NOME_COMPLETO = "Oliver Manoel Anthony Novaes";
    private final Portador PORTADOR = Portador.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();

    @Test
    void criarPortador_deveriaEstarDisponivelNaUrlPadrao() throws Exception{
        String PORTADOR_AS_JSON = objectMapper.writeValueAsString(PORTADOR);

        mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PORTADOR_AS_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void criarPortador_quandoReceberUmPortadorComandoCriarValido__deveriaRetornarPortadorSalvoEHttpOk() throws Exception{
        String PORTADOR_AS_JSON = objectMapper.writeValueAsString(PORTADOR);

        var response = mvc.perform(
                post(URL_DEFAULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PORTADOR_AS_JSON)
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        Assertions.assertEquals(PORTADOR, objectMapper.readValue(response.getContentAsString(), Portador.class));
    }

}