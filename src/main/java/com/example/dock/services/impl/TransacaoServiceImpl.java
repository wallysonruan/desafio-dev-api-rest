package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.repositories.TransacaoRepository;
import com.example.dock.services.TransacaoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final PortadorRepository portadorRepository;
    private Notification notification;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, PortadorRepository portadorRepository, Notification notification) {
        this.transacaoRepository = transacaoRepository;
        this.portadorRepository = portadorRepository;
        this.notification = notification;
    }


    @Override
    public Notification novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto) {
        var transacao = new Transacao();

        if (! portadorRepository.existsById(transacaoComandoCriarDto.portador)){
            notification.addError("Portador não encontrado.");
            return notification;
        }

        var portador = portadorRepository.findById(transacaoComandoCriarDto.portador).get();
        transacao.setPortador(portador);
        transacao.setDateTime(LocalDateTime.now());
        transacao.setTransacaoTipo(transacaoComandoCriarDto.transacaoTipo);
        transacao.setTotalDaTransacao(transacaoComandoCriarDto.totalDaTransacao);

        var response = transacaoRepository.save(transacao);
        notification.setResultado(response);

        return notification;
    }
}
