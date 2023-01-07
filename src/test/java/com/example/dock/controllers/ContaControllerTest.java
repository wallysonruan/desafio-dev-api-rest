package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.services.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContaControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private ContaService service;
    @Mock
    private ContaMapper mapper;

    ContaController controller;

    private final String URL = "/contas";
    private final UUID UUID_DEFAULT = UUID.randomUUID();
    private final Portador PORTADOR = Portador.builder()
            .cpf("18241327005")
            .nome_completo("Oliver Manoel Anthony Novaes")
            .build();
    private final BigDecimal SALDO = BigDecimal.valueOf(13.4);
    private final Agencia AGENCIA = Agencia.builder()
            .uuid(UUID.randomUUID())
            .registro(1l)
            .nome("Centro-BH-MG")
            .build();
    private final Boolean ATIVADA = true;
    private final Boolean BLOQUEADA = false;

    private ContaComandoCriarDTO CONTA_COMANDO_CRIAR_DTO = ContaComandoCriarDTO.builder()
            .portador(PORTADOR)
            .saldo(SALDO)
            .agencia(AGENCIA)
            .build();

    private Conta CONTA = Conta.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();

    private ContaRespostaDTO CONTA_RESPOSTA_DTO = ContaRespostaDTO.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();

    @BeforeEach
    void setUp() {
        controller = new ContaController(service, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void criarConta_quandoReceberContaComandoCriarDtoVálido__retornarHttp200() throws Exception {
        when(service.criarConta(any())).thenReturn(CONTA);
        String contaComandoCriarAsJSON = objectMapper.writeValueAsString(CONTA_COMANDO_CRIAR_DTO);

        mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contaComandoCriarAsJSON)
        ).andExpect(status().isOk());
    }

    @Test
    void criarConta_quandoReceberContaComandoCriarDtoComCpfVálido_retornarHttp200JuntoComContaCriada() throws Exception {
        when(service.criarConta(any())).thenReturn(CONTA);
        when(mapper.contaToContaRespostaDto(CONTA)).thenReturn(CONTA_RESPOSTA_DTO);
        String contaComandoCriarAsJSON = objectMapper.writeValueAsString(CONTA_COMANDO_CRIAR_DTO);

        var response = mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contaComandoCriarAsJSON)
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        var responseToObject = objectMapper.readValue(response.getContentAsString(), Conta.class);

        assertEquals(CONTA.uuid, responseToObject.uuid);
        assertEquals(CONTA.portador, responseToObject.portador);
        assertEquals(CONTA.saldo, responseToObject.saldo);
        assertEquals(CONTA.agencia, responseToObject.agencia);
        assertEquals(CONTA.ativada, responseToObject.ativada);
        assertEquals(CONTA.bloqueada, responseToObject.bloqueada);
    }

    @Test
    void criarConta_quandoNãoReceberContaComandoCriarDto__retornarHttp400() throws Exception{
        mockMvc.perform(
                post(URL)
        ).andExpect(status().isBadRequest());
    }
}