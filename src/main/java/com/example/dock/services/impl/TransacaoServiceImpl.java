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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
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

        try{
            buscarConta(transacaoComandoCriarDto.contaUuid);
            atualizarSaldoDaConta(LocalDate.now(), transacaoComandoCriarDto.transacaoTipo, transacaoComandoCriarDto.totalDaTransacao);
        }catch (NoSuchElementException e){
            notification.addError("Conta não encontrada.");
            return notification;
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
    private void buscarConta(UUID contaUuid){
        Optional<Conta> conta = contaRepository.findById(contaUuid);
        this.conta = conta.get();
    }
    private void atualizarSaldoDaConta(LocalDate date, TransacaoTipo transacaoTipo, BigDecimal valorDaTransacao) {
        if (transacaoTipo == TransacaoTipo.SAQUE){
            saque(date, valorDaTransacao);
        }else{
            deposito(valorDaTransacao);
        }
    }
    private void saque(LocalDate date, BigDecimal valorDaTransacao) {
        if (saquePermitido(date, conta.saldo, valorDaTransacao)) {
            conta.setSaldo(conta.saldo.subtract(valorDaTransacao));
        }else{
            notification.addError("Saldo insuficiente.");
        }
    }
    private boolean saquePermitido(LocalDate date, BigDecimal contaSaldo, BigDecimal valorDaTransacao){
        BigDecimal saldoFinal = contaSaldo.subtract(valorDaTransacao);

        if (saldoFinal.compareTo(BigDecimal.ZERO) == -1 || limiteDiarioDeSaqueExtrapolado(date, valorDaTransacao)){
            return false;
        }
        return true;
    }

    private boolean limiteDiarioDeSaqueExtrapolado(LocalDate date, BigDecimal valorDaTransacao) {
        List<Transacao> listaDeTransacoesEfetuadas;
        List<Transacao> listaDeTransacoesDiarias;
        BigDecimal totalSaqueDiario = valorDaTransacao;

        try {
            listaDeTransacoesEfetuadas = transacaoRepository.findByConta_Uuid(conta.uuid);
            listaDeTransacoesDiarias = listaDeTransacoesEfetuadas.stream().filter(
                    transacao -> transacao.getTransacaoTipo() == TransacaoTipo.SAQUE && transacao.getDateTime().toLocalDate().isEqual(LocalDate.now())).collect(Collectors.toList());

//            listaDeTransacoesDiarias.stream().forEach(transacao -> totalSaqueDiario.add(transacao.getTotalDaTransacao()));

            for (int i = 0; i < listaDeTransacoesDiarias.size(); i++){
                totalSaqueDiario.add(listaDeTransacoesDiarias.get(i).getTotalDaTransacao());
            }

            if (totalSaqueDiario.compareTo(LIMITE_DIARIO_SAQUE) == -1) {
                return false;
            } else {
                notification.addError("Saque não permitido, pois ultrapassaria o limite diário.");
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    private void deposito(BigDecimal valorDaTransacao){
        if (valorDaTransacao.compareTo(BigDecimal.ZERO) == -1 || valorDaTransacao.compareTo(BigDecimal.ZERO) == 0 ){
            notification.addError("Valor de depósito não pode ser menor ou igual a 0.");
        }else {
            conta.setSaldo(conta.saldo.add(valorDaTransacao));
        }
    }
}