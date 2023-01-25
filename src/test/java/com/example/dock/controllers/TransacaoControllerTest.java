package com.example.dock.controllers;

import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.controllers.dtos.TransacaoRespostaDto;
import com.example.dock.models.Portador;
import com.example.dock.models.Transacao;
import com.example.dock.models.TransacaoTipo;
import com.example.dock.services.impl.TransacaoServiceImpl;
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
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    TransacaoController controller;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Mock
    TransacaoServiceImpl service;

    private final String URL = "/transacao";

    private final Transacao TRANSACAO = Transacao.builder()
            .uuid(UUID.randomUUID())
            .dateTime(LocalDateTime.now())
            .transacaoTipo(TransacaoTipo.DEPOSITO)
            .portador(
                    Portador.builder()
                    .uuid(UUID.randomUUID())
                    .build()
            )
            .totalDaTransacao(BigDecimal.valueOf(10.0))
            .build();

    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO = TransacaoComandoCriarDto.builder()
            .portador(TRANSACAO.getPortador().getUuid())
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    private final TransacaoRespostaDto TRANSACAO_RESPOSTA_DTO = TransacaoRespostaDto.builder()
            .uuid(TRANSACAO.getUuid())
            .portador(TRANSACAO.getPortador())
            .dateTime(TRANSACAO.getDateTime())
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    @BeforeEach
    void setUp() {
        controller = new TransacaoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void novaTransacao__quandoReceberTransacaoComandoCriarDtoValio__deveriaRetornarTransacaoRespostaDtoE200() throws Exception {
        when(service.novaTransacao(any())).thenReturn(TRANSACAO);
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
        assertEquals(TRANSACAO_RESPOSTA_DTO, responseAsObject);
    }
}