package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Conta;
import com.example.dock.models.Portador;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.ContaService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContaServiceImpl implements ContaService {

    private ContaRepository contaRepository;
    private PortadorRepository portadorRepository;
    private Notification notification;

    ContaServiceImpl(ContaRepository contaRepository, PortadorRepository portadorRepository, Notification notification){
        this.contaRepository = contaRepository;
        this.portadorRepository = portadorRepository;
        this.notification = notification;
    }

    private Conta conta = new Conta();
    private Portador portador = new Portador();

    @Override
    public Notification criarConta(ContaComandoCriarDTO contaComandoCriarDTO) {

        verificaSePortadorExiste(notification, contaComandoCriarDTO.portador);

        if(notification.hasErrors()){
            return notification;
        }
        portador = portadorRepository.findById(contaComandoCriarDTO.portador).get();
        conta.setPortador(portador);

        notification.setResultado(contaRepository.save(conta));
        return notification;
    }

    private Notification verificaSePortadorExiste(Notification notification, UUID portadorUuid){
        if(! portadorRepository.existsById(portadorUuid)){
            notification.addError("Portador (a) não cadastrado (a).");
            return notification;
        }
        return notification;
    }
}