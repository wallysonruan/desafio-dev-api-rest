package com.example.dock.services.impl;

import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.services.ContaService;
import org.junit.Assert;
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

    private final UUID UUID_DEFAULT = UUID.randomUUID();
    private final Portador PORTADOR = Portador.builder()
            .cpf("18241327005")
            .nome_completo("Oliver Manoel Anthony Novaes")
            .build();
    private final BigDecimal SALDO = BigDecimal.valueOf(13.4);
    private final Agencia AGENCIA = Agencia.builder()
            .uuid(UUID.randomUUID())
            .registro(1l)
            .nome("Centro-BH-MG")
            .build();
    private final Boolean ATIVADA = true;
    private final Boolean BLOQUEADA = false;
    private Conta CONTA = Conta.builder()
            .uuid(UUID_DEFAULT)
            .portador(PORTADOR)
            .agencia(AGENCIA)
            .saldo(SALDO)
            .ativada(ATIVADA)
            .bloqueada(BLOQUEADA)
            .build();

    @BeforeEach
    void setUp(){
        service = new ContaServiceImpl(repository);
    }

    @Test
    void criarConta_quandoReceberUmaContaComCpfNãoCadastrado__deveriaSalvarNoBancoDeDadosERetornarEla(){
        when(repository.existsByPortador_Cpf(CONTA.portador.cpf)).thenReturn(true);
        when(repository.save(CONTA)).thenReturn(CONTA);

        var retorno = service.criarConta(CONTA);

        Assert.assertEquals(retorno, CONTA);
    }

}
