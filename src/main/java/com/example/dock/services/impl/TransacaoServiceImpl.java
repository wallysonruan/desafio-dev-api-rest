package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.repositories.TransacaoRepository;
import com.example.dock.services.TransacaoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private Notification notification;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, ContaRepository contaRepository, Notification notification) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.notification = notification;
    }


    @Override
    public Notification novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto) {
        var transacao = new Transacao();

        if (! contaRepository.existsById(transacaoComandoCriarDto.contaUuid)){
            notification.addError("Conta não encontrada.");
            return notification;
        }

        var conta = contaRepository.findById(transacaoComandoCriarDto.contaUuid).get();
        transacao.setConta(conta);
        transacao.setDateTime(LocalDateTime.now());
        transacao.setTransacaoTipo(transacaoComandoCriarDto.transacaoTipo);
        transacao.setTotalDaTransacao(transacaoComandoCriarDto.totalDaTransacao);

        var response = transacaoRepository.save(transacao);
        notification.setResultado(response);

        return notification;
    }
}
