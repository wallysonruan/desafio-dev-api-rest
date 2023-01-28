package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
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
    private Notification notification;

    private final Transacao TRANSACAO = Transacao.builder()
            .uuid(UUID.randomUUID())
            .conta(Conta.builder()
                    .uuid(UUID.randomUUID())
                    .portador(Portador.builder().build())
                    .bloqueada(false)
                    .ativada(true)
                    .saldo(BigDecimal.valueOf(10))
                    .build())
            .transacaoTipo(TransacaoTipo.DEPOSITO)
            .dateTime(LocalDateTime.now())
            .totalDaTransacao(BigDecimal.valueOf(10.0))
            .build();
    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO = TransacaoComandoCriarDto.builder()
            .contaUuid(TRANSACAO.getConta().uuid)
            .transacaoTipo(TRANSACAO.getTransacaoTipo())
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    private final TransacaoComandoCriarDto TRANSACAO_COMANDO_CRIAR_DTO_SAQUE = TransacaoComandoCriarDto.builder()
            .contaUuid(TRANSACAO.getConta().uuid)
            .transacaoTipo(TransacaoTipo.SAQUE)
            .totalDaTransacao(TRANSACAO.getTotalDaTransacao())
            .build();

    @BeforeEach
    void setUp() {
        notification = new Notification<Transacao>();
        service = new TransacaoServiceImpl(transacaoRepository, contaRepository, notification);
    }

    @Test
    void novaTransacao_quandoTransacaoComandoCriarDtoValido__deveriaConverterParaTransacaoEPersistir(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        verify(transacaoRepository, times(1)).save(any());
        assertNotNull(response);
    }

    @Test
    void novaTransacao_quandoContaExistirRetornarSemErroComTransacaoCompleta(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        assertNotNull(response);
        assertFalse(response.getErrors().contains("Conta não encontrada."));
        assertEquals(TRANSACAO, response.getResultado());
    }

    @Test
    void novaTransacao_quandoContaNaoExistir_RetornarNotificacaoComErro(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.empty());

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        assertNotNull(response);
        assertTrue(response.getErrors().contains("Conta não encontrada."));
    }

    @Test
    void novaTransacao_quandoTransacaoTipoDepositoValido__deveriaAcrescentarAoSaldoDaConta(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        assertEquals(BigDecimal.valueOf(20.0), response.getResultado().getConta().getSaldo());
    }

    @Test
    void novaTransacao_quandoTransacaoTipoSaqueValida__deveriaSubtrairDoSaldoDaConta(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_SAQUE);

        assertEquals(BigDecimal.valueOf(0.0), response.getResultado().getConta().getSaldo());
    }

    @Test
    void novaTransacao_quandoTransacaoTipoSaqueComValorMaiorAoSaldo__deveriaRetornarNotificationComErro(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));

        TransacaoComandoCriarDto transacao_comando_criar_dto_saque_valor_extrapola = TransacaoComandoCriarDto.builder()
            .contaUuid(TRANSACAO.getConta().uuid)
            .transacaoTipo(TransacaoTipo.SAQUE)
            .totalDaTransacao(BigDecimal.valueOf(2000))
            .build();

        var response = service.novaTransacao(transacao_comando_criar_dto_saque_valor_extrapola);

        assertNull(response.getResultado());
        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("Saldo insuficiente."));
    }
}