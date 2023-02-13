package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.repositories.AgenciaRepository;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.ContaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceImplTest {

    @Mock
    ContaRepository contaRepository;
    @Mock
    PortadorRepository portadorRepository;
    @Mock
    AgenciaRepository agenciaRepository;
    ContaService service;
    Notification notification;

    private final UUID UUID_DEFAULT = UUID.randomUUID();
    private final Portador PORTADOR = Portador.builder()
            .uuid(UUID.randomUUID())
            .cpf("18241327005")
            .build();
    private final BigDecimal SALDO = BigDecimal.valueOf(13.4);
    private final Agencia AGENCIA = Agencia.builder()
            .id(1L)
            .nome("Centro-BH-MG")
            .build();
    private final Boolean ATIVADA = true;
    private final Boolean BLOQUEADA = false;
    private final Conta CONTA_ATIVA = Conta.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();

    private final Conta CONTA_DESATIVADA = Conta.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(false)
            .bloqueada(BLOQUEADA)
            .build();
    private final ContaComandoCriarDto CONTA_COMANDO_CRIAR_DTO = ContaComandoCriarDto.builder()
            .agencia(CONTA_ATIVA.getAgencia().getId())
            .portador(CONTA_ATIVA.getPortador().getUuid())
            .build();

    private final ArrayList<Conta> LISTA_DE_CONTA = new ArrayList<>();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new ContaServiceImpl(contaRepository, portadorRepository, agenciaRepository,notification);
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComUuidPortadorValido__deveriaBuscarPortadorAdicionarAContaSalvarNoBancoDeDadosERetornarEla(){
        when(agenciaRepository.existsById(CONTA_ATIVA.getAgencia().id)).thenReturn(true);
        when(portadorRepository.existsById(CONTA_ATIVA.getPortador().getUuid())).thenReturn(true);

        when(portadorRepository.findById(any())).thenReturn(Optional.of(CONTA_ATIVA.getPortador()));
        when(agenciaRepository.findById(any())).thenReturn(Optional.of(CONTA_ATIVA.getAgencia()));
        when(contaRepository.save(any())).thenReturn(CONTA_ATIVA);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        Assertions.assertFalse(retorno.hasErrors());
        Assertions.assertTrue(retorno.getErrors().isEmpty());
        Assertions.assertEquals(CONTA_ATIVA, retorno.getResultado());
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComUuidPortadorJaCadastrado__deveriaRetornarNotificationComErro(){
        when(agenciaRepository.existsById(CONTA_ATIVA.getAgencia().id)).thenReturn(true);
        when(contaRepository.existsByPortador_Uuid(CONTA_ATIVA.getPortador().getUuid())).thenReturn(true);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        verify(portadorRepository, times(1)).existsById(any());
        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertFalse(retorno.getErrors().isEmpty());
        Assertions.assertTrue(retorno.getErrors().contains("Portador já tem conta cadastrada."));
        Assertions.assertNull(retorno.getResultado());
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComUuidPortadorNaoCadastrado__deveriaRetornarNotificationComErro(){
        when(agenciaRepository.existsById(CONTA_ATIVA.getAgencia().id)).thenReturn(true);
        when(portadorRepository.existsById(CONTA_ATIVA.getPortador().getUuid())).thenReturn(false);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        verify(portadorRepository, times(1)).existsById(any());
        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertNotNull(retorno.getErrors());
        Assertions.assertTrue(retorno.getErrors().contains("Portador não cadastrado."));
        Assertions.assertNull(retorno.getResultado());
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComAgenciaNaoCadastrada__deveriaRetornarNotificationComErro(){
        when(agenciaRepository.existsById(CONTA_ATIVA.getAgencia().getId())).thenReturn(false);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        verify(agenciaRepository, times(1)).existsById(any());
        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertNotNull(retorno.getErrors());
        Assertions.assertTrue(retorno.getErrors().contains("Agência não cadastrada."));
        Assertions.assertNull(retorno.getResultado());
    }

    @Test
    void getAll__deveriaRetornarTodasAsContasRegistradas(){
        LISTA_DE_CONTA.add(CONTA_ATIVA);
        LISTA_DE_CONTA.add(CONTA_ATIVA);
        LISTA_DE_CONTA.add(CONTA_ATIVA);
        notification.setResultado(LISTA_DE_CONTA);

        when(contaRepository.findAll()).thenReturn(LISTA_DE_CONTA);

        var retorno = service.getAll();

        verify(contaRepository, times(1)).findAll();
        Assertions.assertNotNull(retorno.getResultado());
        Assertions.assertEquals(retorno.getResultado(), LISTA_DE_CONTA);
    }

    @Test
    void deleteConta_quandoReceberUuidValidoDeContaCadastradaAtiva__deveriaDeletar(){
        when(contaRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(CONTA_ATIVA));
        when(contaRepository.save(CONTA_ATIVA)).thenReturn(CONTA_ATIVA);

        var retorno = service.deleteConta(UUID_DEFAULT);

        verify(contaRepository, times(1)).findById(UUID_DEFAULT);
        verify(contaRepository, times(1)).save(CONTA_ATIVA);
        Assertions.assertFalse(retorno.hasErrors());
        Assertions.assertNull(retorno.getResultado());
    }

    @Test
    void deleteConta_quandoReceberUuidValidoDeContaCadastradaDesativada__deveriaRetornarErro(){
        when(contaRepository.findById(UUID_DEFAULT)).thenReturn(Optional.of(CONTA_DESATIVADA));

        var retorno = service.deleteConta(UUID_DEFAULT);

        verify(contaRepository, times(1)).findById(UUID_DEFAULT);
        verify(contaRepository, times(0)).save(CONTA_DESATIVADA);
        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertTrue(retorno.getErrors().contains("Conta desativada."));
        Assertions.assertNull(retorno.getResultado());
    }

    @Test
    void deleteConta_quandoReceberUuidValidoDeContaNaoCadastrada__deveriaRetornarErro(){
        when(contaRepository.findById(UUID_DEFAULT)).thenReturn(Optional.empty());

        var retorno = service.deleteConta(UUID_DEFAULT);

        verify(contaRepository, times(1)).findById(UUID_DEFAULT);
        verify(contaRepository, times(0)).save(CONTA_DESATIVADA);
        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertTrue(retorno.getErrors().contains("Conta não encontrada."));
        Assertions.assertNull(retorno.getResultado());
    }
}
