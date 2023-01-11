package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDTO;

public interface ContaService {
    Notification criarConta(ContaComandoCriarDTO contaComandoCriarDTO);
}
