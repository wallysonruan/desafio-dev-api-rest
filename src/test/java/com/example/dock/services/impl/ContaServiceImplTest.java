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
            .cpf("18241327005")
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
            .agencia(CONTA.getAgencia().getId())
            .portador(CONTA.getPortador().getUuid())
            .build();

    private final ArrayList<Conta> LISTA_DE_CONTA = new ArrayList<>();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new ContaServiceImpl(contaRepository, portadorRepository, agenciaRepository,notification);
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComUuidPortadorValida__deveriaBuscarPortadorAdicionarAContaSalvarNoBancoDeDadosERetornarEla(){
        notification.setResultado(CONTA);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        Assertions.assertEquals(CONTA, retorno.getResultado());
    }

    @Test
    void getAll__deveriaRetornarTodasAsContasRegistradas(){
        LISTA_DE_CONTA.add(CONTA);
        LISTA_DE_CONTA.add(CONTA);
        LISTA_DE_CONTA.add(CONTA);
        notification.setResultado(LISTA_DE_CONTA);

        when(contaRepository.findAll()).thenReturn(LISTA_DE_CONTA);

        var retorno = service.getAll();

        verify(contaRepository, times(1)).findAll();
        Assertions.assertNotNull(retorno.getResultado());
        Assertions.assertEquals(retorno.getResultado(), LISTA_DE_CONTA);
    }
}
