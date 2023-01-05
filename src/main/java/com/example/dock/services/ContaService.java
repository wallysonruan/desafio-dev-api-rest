package com.example.dock.services;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Conta;

public interface ContaService {
    Conta criarConta(Conta conta);
}
