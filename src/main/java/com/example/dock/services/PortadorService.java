package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.PortadorComandoCriarDto;

import java.util.UUID;

public interface PortadorService {
    Notification criarPortador(PortadorComandoCriarDto portadorComandoCriarDto);
    Notification deletarPortador(UUID uuid);
}
