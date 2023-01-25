package com.example.dock.services;

import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;

public interface TransacaoService {
    Transacao novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto);
}
