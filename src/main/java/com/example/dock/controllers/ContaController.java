package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.services.ContaService;
import com.example.dock.services.impl.ContaServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("contas")
@NoArgsConstructor
public class ContaController {

    private ContaService service;

    public ContaController(ContaService contaService) {
        this.service = contaService;
    }

    @PostMapping
    public ContaRespostaDTO criarConta(@RequestBody ContaComandoCriarDTO contaComandoCriarDTO){
    var ser = service.criarConta(contaComandoCriarDTO);
        return ContaRespostaDTO.contaToContaRespostaDto(ser);
    }
}