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

        verificaSePortadorExiste(contaComandoCriarDTO.portador);
        verificaSeAgenciaExiste(contaComandoCriarDTO.agencia);

        if(this.notification.hasErrors()){
            return this.notification;
        }
        this.portador = portadorRepository.findById(contaComandoCriarDTO.portador).get();
        this.conta.setPortador(this.portador);

        this.agencia = agenciaRepository.findById(contaComandoCriarDTO.agencia).get();
        this.conta.setAgencia(this.agencia);

        this.conta.setSaldo(contaComandoCriarDTO.saldo);
        this.conta.setAtivada(true);
        this.conta.setBloqueada(false);
        this.conta = contaRepository.save(this.conta);

        this.notification.setResultado(this.conta);
        return this.notification;
    }

    @Override
    public Notification getAll() {
        this.notification.setResultado(contaRepository.findAll());
        return this.notification;
    }

    private void verificaSePortadorExiste(UUID portadorUuid){
        if(portadorRepository.existsById(portadorUuid)){
            this.notification.addError("Portador já tem conta cadastrada.");
        }
    }
    private void verificaSeAgenciaExiste(Long agenciaId){
        if(! agenciaRepository.existsById(agenciaId)){
            this.notification.addError("Agência não cadastrada.");
        }
    }
}