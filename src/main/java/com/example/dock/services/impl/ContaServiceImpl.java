package com.example.dock.services.impl;

import com.example.dock.Notification;
import com.example.dock.models.Conta;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.services.ContaService;
import org.springframework.stereotype.Service;

@Service
public class ContaServiceImpl implements ContaService {

    private ContaRepository repository;
    private Notification notification;

    ContaServiceImpl(ContaRepository repository, Notification notification){
        this.repository = repository;
        this.notification = notification;
    }

    @Override
    public Notification criarConta(Conta conta) {

        if(repository.existsByPortador_Cpf(conta.portador.cpf)){
            notification.addError("CPF já cadastrado.");
            return notification;
        }

        notification.setResultado(repository.save(conta));
        return notification;
    }
}
