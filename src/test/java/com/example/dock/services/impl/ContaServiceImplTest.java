package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContaServiceImplTest {

    @Mock
    ContaRepository contaRrepository;
    @Mock
    PortadorRepository portadorRepository;
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
    private final ContaComandoCriarDTO CONTA_COMANDO_CRIAR_DTO = ContaComandoCriarDTO.builder()
            .agencia(CONTA.getAgencia())
            .portador(CONTA.getPortador().getUuid())
            .build();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new ContaServiceImpl(contaRrepository, portadorRepository, notification);
    }

    @Test
    void criarConta_quandoReceberUmContaComandoCriarDtoComUuidPortadorValida__deveriaBuscarPortadorAdicionarAContaSalvarNoBancoDeDadosERetornarEla(){
        notification.setResultado(CONTA);

        var retorno = service.criarConta(CONTA_COMANDO_CRIAR_DTO);

        Assertions.assertEquals(CONTA, retorno.getResultado());
    }
}
