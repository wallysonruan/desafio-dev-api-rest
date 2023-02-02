package com.example.dock.services;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransacaoService {
    Notification<Transacao> novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto);
    Notification<List<Transacao>> getTransactionsByDate(UUID contaUuid, LocalDate initialDate, LocalDate finalDate);
}
