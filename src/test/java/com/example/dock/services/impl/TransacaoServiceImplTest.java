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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        assertTrue(response.getResultado().getConta().ativada);
        assertFalse(response.getResultado().getConta().bloqueada);
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
        assertTrue(response.getResultado().getConta().ativada);
        assertFalse(response.getResultado().getConta().bloqueada);
        assertFalse(response.hasErrors());
        assertTrue(response.getErrors().isEmpty());
    }
    @Test
    void novaTransacao_quandoTransacaoTipoDepositoComContaDesativada__deveriaRetornarNotificacaoComErro(){
        Conta contaDesativada = Conta.builder()
                .ativada(false)
                .build();

        when(contaRepository.findById(any())).thenReturn(Optional.ofNullable(contaDesativada));

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("A conta está desativada."));
    }
    @Test
    void novaTransacao_quandoTransacaoTipoDepositoComContaBloqueada__deveriaRetornarNotificacaoComErro(){
        Conta contaDesativada = Conta.builder()
                .ativada(true)
                .bloqueada(true)
                .build();

        when(contaRepository.findById(any())).thenReturn(Optional.ofNullable(contaDesativada));

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_DEPOSITO);

        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("A conta está bloqueada."));
    }
    @Test
    void novaTransacao_quandoTransacaoTipoDepositoValorNegativo__deveriaRetornarNotificacaoComErro(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));

        var transacao_comando_criar_dto_deposito_valor_negativo = TransacaoComandoCriarDto.builder()
                .contaUuid(TRANSACAO.getConta().uuid)
                .transacaoTipo(TransacaoTipo.DEPOSITO)
                .totalDaTransacao(BigDecimal.valueOf(-1))
                .build();

        var response = service.novaTransacao(transacao_comando_criar_dto_deposito_valor_negativo);

        assertTrue(response.getErrors().contains("Valor de depósito não pode ser menor ou igual a 0."));
    }
    @Test
    void novaTransacao_quandoTransacaoTipoDepositoValorIgualAZero__deveriaRetornarNotificacaoComErro(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));

        var transacao_comando_criar_dto_deposito_valor_negativo = TransacaoComandoCriarDto.builder()
                .contaUuid(TRANSACAO.getConta().uuid)
                .transacaoTipo(TransacaoTipo.DEPOSITO)
                .totalDaTransacao(BigDecimal.valueOf(0))
                .build();

        var response = service.novaTransacao(transacao_comando_criar_dto_deposito_valor_negativo);

        assertTrue(response.getErrors().contains("Valor de depósito não pode ser menor ou igual a 0."));
    }
    @Test
    void novaTransacao_quandoTransacaoTipoSaqueValida__deveriaSubtrairDoSaldoDaConta(){
        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.save(any())).thenReturn(TRANSACAO);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_SAQUE);

        assertEquals(BigDecimal.valueOf(0.0), response.getResultado().getConta().getSaldo());
        assertTrue(response.getResultado().getConta().ativada);
        assertFalse(response.getResultado().getConta().bloqueada);
        assertFalse(response.hasErrors());
        assertTrue(response.getErrors().isEmpty());
    }
    @Test
    void novaTransacao_quandoTransacaoTipoSaqueComContaDesativada__deveriaRetornarNotificacaoComErro(){
        Conta contaDesativada = Conta.builder()
                .ativada(false)
                .build();

        when(contaRepository.findById(any())).thenReturn(Optional.ofNullable(contaDesativada));

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_SAQUE);

        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("A conta está desativada."));
    }
    @Test
    void novaTransacao_quandoTransacaoTipoSaqueComContaBloqueada__deveriaRetornarNotificacaoComErro(){
        Conta contaDesativada = Conta.builder()
                .ativada(true)
                .bloqueada(true)
                .build();

        when(contaRepository.findById(any())).thenReturn(Optional.ofNullable(contaDesativada));

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_SAQUE);

        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("A conta está bloqueada."));
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
    @Test
    void novaTransacao_quandoSomaDosSaqueDiariosMaisValorSaqueSolicitadoIgualarAoLimiteDiario__deveriaRetornarNotificationComErro(){
        List<Transacao> listaDeTransacao = new ArrayList<>();
        Transacao transacaoHoje = Transacao.builder()
                .uuid(UUID.randomUUID())
                .conta(Conta.builder()
                        .uuid(UUID.randomUUID())
                        .portador(Portador.builder().build())
                        .bloqueada(false)
                        .ativada(true)
                        .saldo(BigDecimal.valueOf(10))
                        .build())
                .transacaoTipo(TransacaoTipo.SAQUE)
                .dateTime(LocalDateTime.now())
                .totalDaTransacao(BigDecimal.valueOf(1900))
                .build();

        Transacao transacaoOntem = Transacao.builder()
                .uuid(UUID.randomUUID())
                .conta(Conta.builder()
                        .uuid(UUID.randomUUID())
                        .portador(Portador.builder().build())
                        .bloqueada(false)
                        .ativada(true)
                        .saldo(BigDecimal.valueOf(10))
                        .build())
                .transacaoTipo(TransacaoTipo.SAQUE)
                .dateTime(LocalDateTime.now().minusDays(1))
                .totalDaTransacao(BigDecimal.valueOf(10.0))
                .build();
        listaDeTransacao.add(transacaoHoje);
        listaDeTransacao.add(transacaoHoje);
        listaDeTransacao.add(transacaoOntem);

        when(contaRepository.findById(TRANSACAO.getConta().getUuid())).thenReturn(Optional.ofNullable(TRANSACAO.getConta()));
        when(transacaoRepository.findByConta_Uuid(TRANSACAO.getConta().uuid)).thenReturn(listaDeTransacao);

        var response = service.novaTransacao(TRANSACAO_COMANDO_CRIAR_DTO_SAQUE);

        verify(transacaoRepository, times(1)).findByConta_Uuid(any());
        assertNull(response.getResultado());
        assertTrue(response.hasErrors());
        assertTrue(response.getErrors().contains("Saque não permitido, pois ultrapassaria o limite diário."));
    }

    @Test
    void getTransactionsByDate_deveriaRetornarNotificacaoComListaDeTransacoes(){
        Transacao transacaoDataDentroDoPeriodoEscolhido = Transacao.builder()
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

        Transacao transacaoDataForaDoPeriodoEscolhido = Transacao.builder()
                .uuid(UUID.randomUUID())
                .conta(Conta.builder()
                        .uuid(UUID.randomUUID())
                        .portador(Portador.builder().build())
                        .bloqueada(false)
                        .ativada(true)
                        .saldo(BigDecimal.valueOf(10))
                        .build())
                .transacaoTipo(TransacaoTipo.DEPOSITO)
                .dateTime(LocalDateTime.now().minusDays(10))
                .totalDaTransacao(BigDecimal.valueOf(10.0))
                .build();


        var listOfTransacao = List.of(transacaoDataDentroDoPeriodoEscolhido, transacaoDataForaDoPeriodoEscolhido, transacaoDataDentroDoPeriodoEscolhido);
        when(contaRepository.findById(any())).thenReturn(Optional.of(TRANSACAO.getConta()));
        when(transacaoRepository.findByConta_Uuid(any())).thenReturn(listOfTransacao);

        var response = service.getTransactionsByDate(TRANSACAO.getConta().uuid, LocalDate.now(), LocalDate.now().plusDays(1));

        assertFalse(response.hasErrors());
        assertTrue(response.getResultado().size() == 2);
        assertEquals(transacaoDataDentroDoPeriodoEscolhido.uuid, response.getResultado().get(1).uuid);
        assertEquals(transacaoDataDentroDoPeriodoEscolhido.transacaoTipo, response.getResultado().get(1).transacaoTipo);
        assertEquals(transacaoDataDentroDoPeriodoEscolhido.totalDaTransacao, response.getResultado().get(1).totalDaTransacao);
        assertEquals(transacaoDataDentroDoPeriodoEscolhido.dateTime, response.getResultado().get(1).dateTime);
        assertEquals(transacaoDataDentroDoPeriodoEscolhido.saldo, response.getResultado().get(1).saldo);
    };
}