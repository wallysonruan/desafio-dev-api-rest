package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;

public interface ContaService {
    Notification criarConta(ContaComandoCriarDto contaComandoCriarDTO);
    Notification getAll();
}
