package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;

public interface TransacaoService {
    Notification novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto);
}
