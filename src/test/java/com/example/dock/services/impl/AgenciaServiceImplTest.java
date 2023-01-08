package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.models.Agencia;
import com.example.dock.repositories.AgenciaRepository;
import com.example.dock.services.AgenciaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgenciaServiceImplTest {

    AgenciaService service;
    @Mock
    AgenciaRepository repository;
    Notification notification;
    private final String AGENCIA_NOME = "BH-CENTRO-MG";
    private final Agencia AGENCIA = Agencia.builder()
            .id(1l)
            .nome(AGENCIA_NOME)
            .build();

    @BeforeEach
    void setUp(){
        notification = new Notification();
        service = new AgenciaServiceImpl(repository, notification);
    }

    @Test
    void criarAgencia_quandoReceberAgenciaValidaNaoCadastrada__deveriaPersistirNoBancoDeDadosERetornarNaNotification(){
        when(repository.existsByNome(AGENCIA.getNome())).thenReturn(false);
        when(repository.save(AGENCIA)).thenReturn(AGENCIA);

        var notification = service.criarAgencia(AGENCIA);
        var notification_resultado_to_agencia = (Agencia) notification.getResultado();

        Assertions.assertEquals(Notification.class, notification.getClass());
        Assertions.assertFalse(notification.hasErrors());
        Assertions.assertNotNull(notification_resultado_to_agencia.getId());
        Assertions.assertNotNull(notification_resultado_to_agencia.getNome());
        verify(repository, times(1)).existsByNome(AGENCIA.getNome());
    }

    @Test
    void criarAgencia_quandoReceberAgenciaValidaJáCadastrada__retornarNaNotificationComErroEMensagem(){
        when(repository.existsByNome(AGENCIA.getNome())).thenReturn(true);

        var notification = service.criarAgencia(AGENCIA);

        Assertions.assertEquals(Notification.class, notification.getClass());
        Assertions.assertTrue(notification.hasErrors());
        Assertions.assertNull(notification.getResultado());
        verify(repository, times(1)).existsByNome(AGENCIA.getNome());
        verify(repository, never()).save(AGENCIA);
    }
}
