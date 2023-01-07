package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.PortadorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortadorServiceImplTest {

    PortadorService service;
    Notification notification;
    @Mock
    PortadorRepository repository;

    private final String CPF = "18241327005";
    private final String NOME_COMPLETO = "Oliver Manoel Anthony Novaes";
    private final Portador PORTADOR = Portador.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new PortadorServiceImpl(repository, notification);
    }

    @Test
    void criarPortador__quandoReceberUmPortadorValidoComCpfNuncaCadastrado__PersistirNoBancoDeDadosERetornar(){
        notification.setResultado(PORTADOR);
        when(repository.save(PORTADOR)).thenReturn(PORTADOR);

        var response = service.criarPortador(PORTADOR);

        Assertions.assertEquals(PORTADOR, response.getResultado());
    }
}
