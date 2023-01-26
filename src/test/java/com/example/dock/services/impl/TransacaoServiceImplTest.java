package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.TransacaoMapper;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Conta;
import com.example.dock.models.Transacao;
import com.example.dock.models.TransacaoTipo;
import com.example.dock.repositories.ContaRepository;
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
    private ContaRepository contaRepository;
    @Mock
    private TransacaoMapper mapper;
    private Notification notification;

    private final Transacao TRANSACAO = Transacao.builder()
            .uuid(UUID.randomUUID())
            .conta(Conta.builder().build())
            .transacaoTipo(TransacaoTipo.DEPOSITO)
            .dateTime(LocalDateTime.now())
            .totalDaTransacao(BigDecimal.valueOf(10.0))
            .build();
    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO = TransacaoComandoCriarDto.builder()
            .contaUuid(TRANSACAO.getConta().uuid)
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    @BeforeEach
    void setUp() {
        notification = new Notification<Transacao>();
        service = new TransacaoServiceImpl(transacaoRepository, contaRepository, notification);
    }

    @Test
    void novaTransacao__quandoReceberUmTransacaoComandoCriarDtoValido__deveriaConverterParaTransacaoEPersistir(){
        when(contaRepository.existsById(TRANSACAO.getConta().getUuid())).thenReturn(true);
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        verify(transacaoRepository, times(1)).save(any());
        assertNotNull(response);
    }

    @Test
    void novaTransacao__quandoContaExistirRetornarSemErroComTransacaoCompleta(){
        when(contaRepository.existsById(TRANSACAO.getConta().getUuid())).thenReturn(true);
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        assertNotNull(response);
        assertFalse(response.getErrors().contains("Conta não encontrada."));
        assertEquals(TRANSACAO, response.getResultado());
    }

    @Test
    void novaTransacao__quandoContaNaoExistirRetornarNotificacaoComErro(){
        when(contaRepository.existsById(TRANSACAO.getConta().getUuid())).thenReturn(false);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO);

        assertNotNull(response);
        assertTrue(response.getErrors().contains("Conta não encontrada."));
    }
}