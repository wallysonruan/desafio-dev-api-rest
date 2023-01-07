package com.example.dock.services.impl;

import com.example.dock.models.Conta;
import com.example.dock.repositories.ContaRepository;
import com.example.dock.services.ContaService;
import org.springframework.stereotype.Service;

@Service
public class ContaServiceImpl implements ContaService {

    private ContaRepository repository;

    ContaServiceImpl(ContaRepository repository){
        this.repository = repository;
    }

    @Override
    public Conta criarConta(Conta conta) {
//        if(! repository.existsByPortador_Cpf(conta.portador.cpf)){
//            return
//        }
        return repository.save(conta);
    }
}
