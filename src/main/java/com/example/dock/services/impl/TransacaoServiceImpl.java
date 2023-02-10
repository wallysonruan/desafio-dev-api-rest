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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private Notification notification;
    private Conta conta = new Conta();
    private final BigDecimal LIMITE_DIARIO_SAQUE = BigDecimal.valueOf(2000);

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, ContaRepository contaRepository, Notification notification) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.notification = notification;
    }
    @Override
    public Notification<Transacao> novaTransacao(TransacaoComandoCriarDto transacaoComandoCriarDto) {
        notification = new Notification<Transacao>();

        buscarConta(transacaoComandoCriarDto.contaUuid);

        if (notification.hasErrors()){
            return notification;
        }else {
            if (conta.ativada){
                if (! conta.bloqueada){
                    atualizarSaldoDaConta(transacaoComandoCriarDto.transacaoTipo, transacaoComandoCriarDto.totalDaTransacao);
                }else{
                    notification.addError("A conta está bloqueada.");
                }
            }else{
                notification.addError("A conta está desativada.");
            }
        }

        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setDateTime(LocalDateTime.now());
        transacao.setTransacaoTipo(transacaoComandoCriarDto.transacaoTipo);
        transacao.setTotalDaTransacao(transacaoComandoCriarDto.totalDaTransacao);
        transacao.setSaldo(conta.getSaldo());

        transacao = transacaoRepository.save(transacao);
        notification.setResultado(transacao);

        return notification;
    }

    @Override
    public Notification<List<Transacao>> getTransactionsByDate(UUID contaUuid, LocalDate initialDate, LocalDate finalDate) {
        notification = new Notification();
        buscarConta(contaUuid);

        if (notification.hasErrors()){
            return notification;
        }

        var todasAsTransacoesDaConta = buscarTransacoesEfetuadas();
        var todasAsTransacoesEntreOPeriodo = todasAsTransacoesDaConta.stream().filter(
                transacao -> verificarSeEstaEntreDuasDatas(transacao.getDateTime().toLocalDate(), initialDate, finalDate)).collect(Collectors.toList());

        notification.setResultado(todasAsTransacoesEntreOPeriodo);
        return notification;
    }

    private boolean verificarSeEstaEntreDuasDatas(LocalDate dateToCheck, LocalDate initialDate, LocalDate finalDate){
        if (dateToCheck.compareTo(initialDate) >= 0 && dateToCheck.compareTo(finalDate) <= 0){
            return true;
        }
        return false;
    }
    private void buscarConta(UUID contaUuid){
        Optional<Conta> conta = contaRepository.findById(contaUuid);
        if (conta.isEmpty()){
            notification.addError("Conta não encontrada.");
        }else {
            this.conta = conta.get();
        }
    }
    private void atualizarSaldoDaConta(TransacaoTipo transacaoTipo, BigDecimal valorDaTransacao) {
        if (transacaoTipo == TransacaoTipo.SAQUE){
            saque(valorDaTransacao);
        }else{
            deposito(valorDaTransacao);
        }
    }
    private List<Transacao> buscarTransacoesEfetuadas(){
        List<Transacao> transacoes = new ArrayList<>();
        try{
            var transacoesDiarias = transacaoRepository.findByConta_Uuid(conta.uuid);
            transacoesDiarias.stream().forEach(transacao -> transacoes.add(transacao));
        }catch (NullPointerException e){
        }
        return transacoes;
    }
    private void deposito(BigDecimal valorDaTransacao){
        if (valorDaTransacao.compareTo(BigDecimal.ZERO) == -1 || valorDaTransacao.compareTo(BigDecimal.ZERO) == 0 ){
            notification.addError("Valor de depósito não pode ser menor ou igual a 0.");
        }else {
            conta.setSaldo(conta.saldo.add(valorDaTransacao));
        }
    }
    private void saque(BigDecimal valorDaTransacao) {
        if (saquePermitido(conta.saldo, valorDaTransacao)) {
            conta.setSaldo(conta.saldo.subtract(valorDaTransacao));
        }
    }
    private boolean saquePermitido(BigDecimal contaSaldo, BigDecimal valorDaTransacao){
        if (saldoInsuficiente(contaSaldo, valorDaTransacao) || limiteDiarioDeSaqueExtrapolado(valorDaTransacao)){
            return false;
        }
        return true;
    }
    private boolean saldoInsuficiente(BigDecimal contaSaldo, BigDecimal valorDaTransacao){
        if (valorDaTransacao.compareTo(contaSaldo) == 1){
            notification.addError("Saldo insuficiente.");
            return true;
        }
        return false;
    }
    private boolean limiteDiarioDeSaqueExtrapolado(BigDecimal valorDaTransacao) {
        List<Transacao> listaDeTransacoesEfetuadas = buscarTransacoesEfetuadas();
        List<Transacao> listaDeTransacoesDiarias;
        List<BigDecimal> valoresSacados = new ArrayList<>();
        BigDecimal totalSaqueDiario;

        if (listaDeTransacoesEfetuadas.isEmpty()){
            return false;
        }else{
            listaDeTransacoesDiarias = listaDeTransacoesEfetuadas.stream()
                    .filter(transacao ->
                        transacao.getTransacaoTipo() == TransacaoTipo.SAQUE && transacao.getDateTime().toLocalDate().isEqual(LocalDate.now())
                     ).collect(Collectors.toList());

            listaDeTransacoesDiarias.forEach(transacao -> {
                valoresSacados.add(transacao.getTotalDaTransacao());
            });

            totalSaqueDiario = valoresSacados.stream().reduce(BigDecimal.valueOf(0), (subtotal, proximo) -> BigDecimal.valueOf(subtotal.intValue() + proximo.intValue()));

            switch (totalSaqueDiario.compareTo(LIMITE_DIARIO_SAQUE)){
                case -1:
                case 0:
                    return false;
                case 1:
                    notification.addError("Saque não permitido, pois ultrapassaria o limite diário.");
                    return true;
            }
        }
        return false;
    }
}