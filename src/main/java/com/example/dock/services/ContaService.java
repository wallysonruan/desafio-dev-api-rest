package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Conta;

public interface ContaService {
    Notification criarConta(ContaComandoCriarDTO contaComandoCriarDTO);
}
