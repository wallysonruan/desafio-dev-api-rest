package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.models.Portador;
import com.example.dock.repositories.PortadorRepository;
import com.example.dock.services.PortadorService;
import org.springframework.stereotype.Service;

@Service
public class PortadorServiceImpl implements PortadorService {

    private PortadorRepository repository;
    private Notification notification;

    PortadorServiceImpl(PortadorRepository repository, Notification notification){
        this.repository = repository;
        this.notification = notification;
    }

    @Override
    public Notification criarPortador(Portador portador) {
        notification.setResultado(repository.save(portador));
        return notification;
    }
}
