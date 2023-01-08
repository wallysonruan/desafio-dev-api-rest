package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.models.Agencia;
import com.example.dock.repositories.AgenciaRepository;
import com.example.dock.services.AgenciaService;
import org.springframework.stereotype.Service;

@Service
public class AgenciaServiceImpl implements AgenciaService {

    private AgenciaRepository repository;
    private Notification notification;

    AgenciaServiceImpl(AgenciaRepository repository, Notification notification){
        this.repository = repository;
        this.notification = notification;
    }

    @Override
    public Notification criarAgencia(Agencia agencia) {
        if(repository.existsByNome(agencia.getNome())){
            notification.addError("Agencia já cadastrada.");
            return notification;
        }
        notification.setResultado(repository.save(agencia));
        return notification;
    }
}
