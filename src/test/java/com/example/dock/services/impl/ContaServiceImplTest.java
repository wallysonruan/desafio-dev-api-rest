package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.services.ContaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContaServiceImplTest {

    @Mock
    ContaRepository repository;
    ContaService service;
    Notification notification;

    private final UUID UUID_DEFAULT = UUID.randomUUID();
    private final Portador PORTADOR = Portador.builder()
            .cpf("18241327005")
            .build();
    private final BigDecimal SALDO = BigDecimal.valueOf(13.4);
    private final Agencia AGENCIA = Agencia.builder()
            .uuid(UUID.randomUUID())
            .registro(1L)
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

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new ContaServiceImpl(repository, notification);
    }

    @Test
    void criarConta_quandoReceberUmaContaComCpfNaoCadastrado__deveriaSalvarNoBancoDeDadosERetornarEla(){
        notification.setResultado(CONTA);
        when(repository.existsByPortador_Cpf(CONTA.portador.cpf)).thenReturn(false);
        when(repository.save(CONTA)).thenReturn(CONTA);

        var retorno = service.criarConta(CONTA);

        Assertions.assertEquals(CONTA, retorno.getResultado());
    }

    @Test
    void criarConta_quandoReceberUmaContaComCpfJaCadastrado__deveriaRetornarNotificacaoComErro(){
        when(repository.existsByPortador_Cpf(CONTA.portador.cpf)).thenReturn(true);

        var retorno = service.criarConta(CONTA);

        Assertions.assertTrue(retorno.hasErrors());
        Assertions.assertEquals("CPF já cadastrado.", retorno.getErrors());
    }
}
