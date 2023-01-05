package com.example.dock.services.impl;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Conta;
import com.example.dock.services.ContaService;
import org.springframework.stereotype.Service;

@Service
public class ContaServiceImpl implements ContaService {
    @Override
    public Conta criarConta(ContaComandoCriarDTO contaComandoCriarDTO) {
        return Conta.contaComandoCriarToConta(contaComandoCriarDTO);
    }
}
