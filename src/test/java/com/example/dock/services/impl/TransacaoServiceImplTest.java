package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.TransacaoMapper;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.models.Transacao;
import com.example.dock.models.TransacaoTipo;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.repositories.TransacaoRepository;
import com.example.dock.services.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceImplTest {

    private TransacaoService service;
    @Mock
    private TransacaoRepository transacaoRepository;
    @Mock
    private PortadorRepository portadorRepository;
    @Mock
    private TransacaoMapper mapper;
    private Notification notification;

    private final Transacao TRANSACAO = Transacao.builder()
            .uuid(UUID.randomUUID())
            .portador(Portador.builder().build())
            .transacaoTipo(TransacaoTipo.DEPOSITO)
            .dateTime(LocalDateTime.now())
            .totalDaTransacao(BigDecimal.valueOf(10.0))
            .build();
    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO = TransacaoComandoCriarDto.builder()
            .portador(TRANSACAO.getPortador().uuid)
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    @BeforeEach
    void setUp() {
        notification = new Notification<Transacao>();
        service = new TransacaoServiceImpl(transacaoRepository, portadorRepository, notification);
    }

    @Test
    void novaTransacao__quandoReceberUmTransacaoComandoCriarDtoValido__deveriaConverterParaTransacaoEPersistir(){
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        verify(transacaoRepository, times(1)).save(any());
        assertNotNull(response);
    }

    @Test
    void novaTransacao__quandoPortadorExistirRetornarSemErroComTransacaoCompleta(){
        when(portadorRepository.existsById(TRANSACAO.getPortador().getUuid())).thenReturn(true);
        when(portadorRepository.findById(TRANSACAO.getPortador().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getPortador()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        assertNotNull(response);
        assertFalse(response.getErrors().contains("Portador não encontrado."));
        assertEquals(TRANSACAO, response.getResultado());
    }

    @Test
    void novaTransacao__quandoPortadorNaoExistirRetornarNotificacaoComErro(){
        when(portadorRepository.existsById(TRANSACAO.getPortador().getUuid())).thenReturn(false);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        assertNotNull(response);
        assertTrue(response.getErrors().contains("Portador não encontrado."));
    }
}