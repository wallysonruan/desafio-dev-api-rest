package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.controllers.dtos.TransacaoRespostaDto;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.models.Transacao;
import com.example.dock.models.TransacaoTipo;
import com.example.dock.services.impl.TransacaoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    TransacaoController controller;
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    Notification notification;

    @Mock
    TransacaoServiceImpl service;
    @Mock
    TransacaoMapperImpl transacaoMapper;

    private final String URL = "/transacao";

    private final Transacao TRANSACAO = Transacao.builder()
            .uuid(UUID.randomUUID())
            .dateTime(LocalDateTime.now())
            .transacaoTipo(TransacaoTipo.DEPOSITO)
            .conta(
                    Conta.builder()
                    .uuid(UUID.randomUUID())
                    .build()
            )
            .totalDaTransacao(BigDecimal.valueOf(10.0))
            .build();

    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO = TransacaoComandoCriarDto.builder()
            .contaUuid(TRANSACAO.getConta().getUuid())
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    private final TransacaoRespostaDto TRANSACAO_RESPOSTA_DTO = TransacaoRespostaDto.builder()
            .uuid(TRANSACAO.getUuid())
            .conta(TRANSACAO.getConta())
            .dateTime(TRANSACAO.getDateTime())
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    @BeforeEach
    void setUp() {
        controller = new TransacaoController(service, transacaoMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        notification = new Notification();
    }

    @Test
    void novaTransacao__quandoReceberTransacaoComandoCriarDtoValido__deveriaRetornarTransacaoRespostaDtoE200() throws Exception {
        notification.setResultado(TRANSACAO);
        when(service.novaTransacao(any())).thenReturn(notification);
        when(transacaoMapper.transacaoToTransacaoDto(TRANSACAO)).thenReturn(TRANSACAO_RESPOSTA_DTO);
        String TRANSACAO_COMANDO_CRIAR_DTO_JSON = objectMapper.writeValueAsString(TRANSACAO_COMANDO_CRIAR_DTO);

        var response = mockMvc.perform(
                post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TRANSACAO_COMANDO_CRIAR_DTO_JSON)
        ).andExpect(status().isCreated())
                .andReturn().getResponse();

        var responseAsObject = objectMapper.readValue(response.getContentAsString(), TransacaoRespostaDto.class);

        verify(service, times(1)).novaTransacao(any());
        assertNotNull(responseAsObject);
        assertEquals(TRANSACAO_RESPOSTA_DTO.uuid, responseAsObject.uuid);
        assertEquals(TRANSACAO_RESPOSTA_DTO.totalDaTransacao, responseAsObject.totalDaTransacao);
        assertEquals(TRANSACAO_RESPOSTA_DTO.transacaoTipo, responseAsObject.transacaoTipo);
        assertEquals(TRANSACAO_RESPOSTA_DTO.conta, responseAsObject.conta);
    }

    @Test
    void novaTransacao__quandoReceberContaInexistente__deveriaRetornarHttps403ENotificationComErro() throws Exception {
        String mensagemDeErro = "Conta não encontrada.";
        notification.addError(mensagemDeErro);
        when(service.novaTransacao(any())).thenReturn(notification);
        String TRANSACAO_COMANDO_CRIAR_DTO_JSON = objectMapper.writeValueAsString(TRANSACAO_COMANDO_CRIAR_DTO);

        var response = mockMvc.perform(
                        post(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TRANSACAO_COMANDO_CRIAR_DTO_JSON)
                ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        verify(service, times(1)).novaTransacao(any());
        assertTrue(response.getContentAsString().contains(mensagemDeErro));
    }

    @Test
    void novaTransacao__quandoReceberTransacaoComandoCriarDtoNull__deveriaRetornarErro404() throws Exception {
        TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO_NULL = TransacaoComandoCriarDto.builder().build();
        String TRANSACAO_COMANDO_CRIAR_DTO_NULL_AS_JSON = objectMapper.writeValueAsString(TRANSACAO_COMANDO_CRIAR_DTO);

        var response = mockMvc.perform(
                        post(URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("")
                ).andExpect(status().isBadRequest());

        verify(service, times(0)).novaTransacao(any());
    }
}