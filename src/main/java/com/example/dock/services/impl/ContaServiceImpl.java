package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;
import com.example.dock.models.Agencia;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.repositories.AgenciaRepository;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.ContaService;
import org.springframework.stereotype.Service;

import java.util.IllegalFormatCodePointException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final PortadorRepository portadorRepository;
    private final AgenciaRepository agenciaRepository;
    private Notification notification;

    ContaServiceImpl(ContaRepository contaRepository, PortadorRepository portadorRepository, AgenciaRepository agenciaRepository, Notification notification){
        this.contaRepository = contaRepository;
        this.portadorRepository = portadorRepository;
        this.agenciaRepository = agenciaRepository;
        this.notification = notification;
    }

    private Conta conta = new Conta();
    private Portador portador = new Portador();
    private Agencia agencia = new Agencia();

    @Override
    public Notification criarConta(ContaComandoCriarDto contaComandoCriarDTO) {
        notification.clearErrors();

        verificaSePortadorExiste(contaComandoCriarDTO.portador);
        verificaSePortadorJaTemConta(contaComandoCriarDTO.portador);
        verificaSeAgenciaExiste(contaComandoCriarDTO.agencia);

        if(notification.hasErrors()){
            return notification;
        }
        portador = portadorRepository.findById(contaComandoCriarDTO.portador).get();
        conta.setPortador(portador);

        agencia = agenciaRepository.findById(contaComandoCriarDTO.agencia).get();
        conta.setAgencia(agencia);

        conta.setSaldo(contaComandoCriarDTO.saldo);
        conta.setAtivada(true);
        conta.setBloqueada(false);
        conta = contaRepository.save(conta);

        notification.setResultado(conta);
        return notification;
    }

    @Override
    public Notification getAll() {
        notification.setResultado(contaRepository.findAll());
        return notification;
    }

    @Override
    public Notification deleteConta(UUID contaUuid) {
        try{
            this.conta = contaRepository.findById(contaUuid).get();
            if(this.conta.ativada){
                this.conta.setAtivada(false);
                contaRepository.save(this.conta);
            }else {
                notification.clearErrors();
                notification.addError("Conta desativada.");
            }
            return notification;
        }catch (NoSuchElementException exception){
            notification.clearErrors();
            notification.addError("Conta não encontrada.");
        }
        return notification;
    }

    private void verificaSePortadorExiste(UUID portadorUuid){
        if(! portadorRepository.existsById(portadorUuid)){
            notification.addError("Portador não cadastrado.");
        }
    }
    private void verificaSePortadorJaTemConta(UUID portadorUuid){
        if(contaRepository.existsByPortador_Uuid(portadorUuid)){
            notification.addError("Portador já tem conta cadastrada.");
        }
    }
    private void verificaSeAgenciaExiste(Long agenciaId){
        if(! agenciaRepository.existsById(agenciaId)){
            notification.addError("Agência não cadastrada.");
        }
    }
}