package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;
import com.example.dock.controllers.dtos.ContaRespostaDto;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.services.ContaService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    Notification notification;

    ContaController controller;

    private final String URL = "/contas";
    private final UUID UUID_DEFAULT = UUID.randomUUID();
    private final Portador PORTADOR = Portador.builder()
            .cpf("18241327005")
            .nomeCompleto("Oliver Manoel Anthony Novaes")
            .build();
    private final BigDecimal SALDO = BigDecimal.valueOf(13.4);
    private final Agencia AGENCIA = Agencia.builder()
            .id(1L)
            .nome("Centro-BH-MG")
            .build();
    private final Boolean ATIVADA = true;
    private final Boolean BLOQUEADA = false;

    private final Conta CONTA = Conta.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();
    private final ContaComandoCriarDto CONTA_COMANDO_CRIAR_DTO = ContaComandoCriarDto.builder()
            .portador(CONTA.getPortador().getUuid())
            .saldo(SALDO)
            .agencia(CONTA.getAgencia().getId())
            .build();

    private final ContaRespostaDto CONTA_RESPOSTA_DTO = com.example.dock.controllers.dtos.ContaRespostaDto.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();

    private ArrayList<Conta> LISTA_DE_CONTAS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        controller = new ContaController(service, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        notification = new Notification();

        LISTA_DE_CONTAS.add(CONTA);
        LISTA_DE_CONTAS.add(CONTA);
        LISTA_DE_CONTAS.add(CONTA);
    }

    @Test
    void criarConta_quandoReceberContaComandoCriarDtoValido_retornarHttp200JuntoComContaCriada() throws Exception {
        notification.setResultado(CONTA);
        when(service.criarConta(any())).thenReturn(notification);
        when(mapper.contaToContaRespostaDto(CONTA)).thenReturn(CONTA_RESPOSTA_DTO);

        var response = mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CONTA_COMANDO_CRIAR_DTO))
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        var responseAsObject = objectMapper.readValue(response.getContentAsString(), ContaRespostaDto.class);

        assertNotNull(responseAsObject);
        assertEquals(CONTA.getUuid(), responseAsObject.uuid);
        assertEquals(CONTA.getPortador(), responseAsObject.portador);
        assertEquals(CONTA.getSaldo(), responseAsObject.saldo);
        assertEquals(CONTA.getAgencia().getId(), responseAsObject.agencia.getId());
        assertEquals(CONTA.getAgencia().getNome(), responseAsObject.agencia.getNome());
        assertEquals(CONTA.getAtivada(), responseAsObject.ativada);
        assertEquals(CONTA.getBloqueada(), responseAsObject.bloqueada);
    }

    @Test
    void criarConta_quandoNaoReceberContaComandoCriarDto__retornarHttp400() throws Exception{
        mockMvc.perform(
                post(URL)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void criarConta_quandoContaComandoCriarDtoPortadorExistente__retornarHttp403ENotificationComErro() throws Exception{
        String mensagemErro = "Portador já tem conta cadastrada.";
        notification.addError(mensagemErro);
        when(service.criarConta(any())).thenReturn(notification);

        var response = mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CONTA_COMANDO_CRIAR_DTO))
        ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        Assertions.assertTrue(response.getContentAsString().contains(mensagemErro));
    }

    @Test
    void getConta_quandoReceberRequisicaoGet__deveriaRetornarTodasAsContasRegistradas() throws Exception {
        notification.setResultado(LISTA_DE_CONTAS);
        when(service.getAll()).thenReturn(notification);

        var response = mockMvc.perform(
                get(URL)
        ).andExpect(status().isOk())
                .andReturn().getResponse();

        var responseAsListOfObjects = objectMapper.readValue(response.getContentAsString(), new TypeReference<ArrayList<Conta>>() {
        });

        verify(service, times(1)).getAll();
        assertNotNull(response.getContentType());
        assertEquals(responseAsListOfObjects.get(0).getClass(), Conta.class);
        assertEquals(LISTA_DE_CONTAS.get(0).getUuid(), responseAsListOfObjects.get(0).getUuid());
        assertEquals(LISTA_DE_CONTAS.get(0).getBloqueada(), responseAsListOfObjects.get(0).getBloqueada());
        assertEquals(LISTA_DE_CONTAS.get(0).getAtivada(), responseAsListOfObjects.get(0).getAtivada());
        assertEquals(LISTA_DE_CONTAS.get(0).getPortador(), responseAsListOfObjects.get(0).getPortador());
        assertEquals(LISTA_DE_CONTAS.get(0).getSaldo(), responseAsListOfObjects.get(0).getSaldo());
    }
}