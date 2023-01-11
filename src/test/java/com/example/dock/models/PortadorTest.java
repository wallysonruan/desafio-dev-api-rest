package com.example.dock.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PortadorTest {

    Portador portador;

    @BeforeEach
    void setUp(){
        portador = new Portador();
    }

    @Test
    void setCpf_quandoReceberUmCpfComPontuacao__deveriaRemoverTodaAPontuacao() {
        portador.setCpf("992.616.560-56");

        Assertions.assertEquals("99261656056", portador.getCpf());
    }
}