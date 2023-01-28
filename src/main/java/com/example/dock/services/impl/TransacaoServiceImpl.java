package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Conta;
import com.example.dock.models.Transacao;
import com.example.dock.models.TransacaoTipo;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.repositories.TransacaoRepository;
import com.example.dock.services.TransacaoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private Notification notification;
    private Conta conta = new Conta();

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, ContaRepository contaRepository, Notification notification) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.notification = notification;
    }

    @Override
    public Notification<Transacao> novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto) {
        notification = new Notification<Transacao>();

        try{
            buscarConta(transacaoComandoCriarDto.contaUuid);
            atualizarSaldoDaConta(transacaoComandoCriarDto.transacaoTipo, transacaoComandoCriarDto.totalDaTransacao);
        }catch (NoSuchElementException e){
            notification.addError("Conta não encontrada.");
            return notification;
        }

        var transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setDateTime(LocalDateTime.now());
        transacao.setTransacaoTipo(transacaoComandoCriarDto.transacaoTipo);
        transacao.setTotalDaTransacao(transacaoComandoCriarDto.totalDaTransacao);
        transacao.setSaldo(conta.getSaldo());

        transacao = transacaoRepository.save(transacao);
        notification.setResultado(transacao);

        return notification;
    }
    private void buscarConta(UUID contaUuid){
        Optional<Conta> conta = contaRepository.findById(contaUuid);
        this.conta = conta.get();
    }
    private void atualizarSaldoDaConta(TransacaoTipo transacaoTipo, BigDecimal valorDaTransacao) {
        if (transacaoTipo == TransacaoTipo.SAQUE){
            saque(valorDaTransacao);
        }else{
            deposito(valorDaTransacao);
        }
    }
    private void saque(BigDecimal valorDaTransacao) {
        if (saquePermitido(conta.saldo, valorDaTransacao)) {
            conta.setSaldo(conta.saldo.subtract(valorDaTransacao));
        }else{
            notification.addError("Saldo insuficiente.");
        }
    }
    private boolean saquePermitido(BigDecimal contaSaldo, BigDecimal valorDaTransacao){
        BigDecimal saldoFinal = contaSaldo.subtract(valorDaTransacao);

        if (saldoFinal.compareTo(BigDecimal.ZERO) == -1){
            return false;
        }
        return true;
    }
    private void deposito(BigDecimal valorDaTransacao){
        conta.setSaldo(conta.saldo.add(valorDaTransacao));
    }
}