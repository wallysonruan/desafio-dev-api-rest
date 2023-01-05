package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.services.ContaService;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("contas")
@NoArgsConstructor
public class ContaController {

    private ContaService service;
    private ContaMapper mapper;

    public ContaController(ContaService contaService, ContaMapper mapper) {
        this.service = contaService;
        this.mapper = mapper;
    }

    @PostMapping
    public ContaRespostaDTO criarConta(@RequestBody ContaComandoCriarDTO contaComandoCriarDTO){
        return mapper.contaToContaRespostaDto(
                service.criarConta(
                        mapper.contaComandoCriarToConta(contaComandoCriarDTO)
                )
        );
    }
}