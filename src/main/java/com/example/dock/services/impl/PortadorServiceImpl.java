package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.PortadorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PortadorServiceImpl implements PortadorService {

    private PortadorRepository repository;
    private Notification notification;
    private Portador portador;

    PortadorServiceImpl(PortadorRepository repository, Notification notification){
        this.repository = repository;
        this.notification = notification;
    }

    @Override
    public Notification criarPortador(PortadorComandoCriarDto portadorComandoCriarDto) {
        notification.clearErrors();
        portador = new Portador();

        if(repository.existsByCpf(portadorComandoCriarDto.cpf)){
           notification.addError("CPF já cadastrado.");
           return notification;
        };

        portador.setCpf(portadorComandoCriarDto.getCpf());
        portador.setNomeCompleto(portadorComandoCriarDto.getNomeCompleto());
        notification.setResultado(repository.save(portador));
        return notification;
    }

    @Override
    public Notification deletarPortador(UUID uuid) {
        if(repository.existsById(uuid)){
            repository.deleteById(uuid);
            return notification;
        }
        notification.addError("Portador (a) não cadastrado (a).");
        return notification;
    }
}
