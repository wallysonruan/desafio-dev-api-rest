package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;

public interface TransacaoService {
    Notification<Transacao> novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto);
}
