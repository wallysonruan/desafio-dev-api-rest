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

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortadorServiceImplTest {

    PortadorService service;
    Notification notification;
    @Mock
    PortadorRepository repository;

    private final String CPF = "18241327005";
    private final String NOME_COMPLETO = "Oliver Manoel Anthony Novaes";
    private final UUID UUID_DEFAULT = UUID.fromString("873e2fcb-d3a1-454c-91cb-c2422f6958f3");
    private final Portador PORTADOR = Portador.builder()
            .uuid(UUID.randomUUID())
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();
    private final Portador PORTADOR_SEM_UUID = Portador.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();
    private final PortadorComandoCriarDto PORTADOR_COMANDO_CRIAR_DTO = PortadorComandoCriarDto.builder()
            .cpf(CPF)
            .nome_completo(NOME_COMPLETO)
            .build();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new PortadorServiceImpl(repository, notification);
    }

    @Test
    void criarPortador__quandoReceberUmPortadorComandoCriarDtoValidoComCpfNuncaCadastrado__persistirNoBancoDeDadosERetornar(){
        notification.setResultado(PORTADOR);
        when(repository.existsByCpf(PORTADOR.getCpf())).thenReturn(false);
        when(repository.save(PORTADOR_SEM_UUID)).thenReturn(PORTADOR);

        var response = service.criarPortador(PORTADOR_COMANDO_CRIAR_DTO);

        Assertions.assertEquals(PORTADOR, response.getResultado());
    }

    @Test
    void criarPortador__quandoReceberUmPortadorComandoCriarDtoValidoComCpfJaCadastrado__persistirNoBancoDeDadosERetornar(){
        when(repository.existsByCpf(PORTADOR.getCpf())).thenReturn(true);
        var response = service.criarPortador(PORTADOR_COMANDO_CRIAR_DTO);

        Assertions.assertTrue(response.hasErrors());
        Assertions.assertTrue(response.getErrors().contains("CPF já cadastrado."));
        Assertions.assertNull(response.getResultado());
    }

    @Test
    void deletarPortador_quandoReceberUuidDePortadorRegistrado__deveriaDeletarDoBancoDeDados(){
        when(repository.existsById(UUID_DEFAULT)).thenReturn(true);
        doNothing().when(repository).deleteById(UUID_DEFAULT);

        var response = service.deletarPortador(UUID_DEFAULT);

        verify(repository, times(1)).deleteById(UUID_DEFAULT);
        Assertions.assertFalse(notification.hasErrors());
    }

    @Test
    void deletarPortador_quandoReceberUuidDePortadorNãoRegistrado__deveriaRetornarNotificacaoComErro(){
        when(repository.existsById(UUID_DEFAULT)).thenReturn(false);

        var response = service.deletarPortador(UUID_DEFAULT);

        verify(repository, never()).deleteById(UUID_DEFAULT);
        verify(repository, times(1)).existsById(UUID_DEFAULT);
        Assertions.assertTrue(notification.getErrors().contains("Portador (a) não cadastrado (a)."));
        Assertions.assertTrue(notification.hasErrors());
    }
}
