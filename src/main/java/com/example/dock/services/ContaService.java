package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;
import com.example.dock.models.Conta;

import java.util.ArrayList;

public interface ContaService {
    Notification<Conta> criarConta(ContaComandoCriarDto contaComandoCriarDTO);
    Notification<ArrayList<Conta>> getAll();
}
