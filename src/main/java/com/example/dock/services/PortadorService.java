package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.models.Portador;

import java.util.UUID;

public interface PortadorService {
    Notification criarPortador(Portador portador);
    Notification deletarPortador(UUID uuid);
}
