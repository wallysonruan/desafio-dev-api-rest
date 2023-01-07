package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.controllers.dtos.ContaRespostaDTO;
import com.example.dock.services.ContaService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ContaRespostaDTO> criarConta(@RequestBody ContaComandoCriarDTO contaComandoCriarDTO){
        var response = mapper.contaToContaRespostaDto(
                service.criarConta(
                        mapper.contaComandoCriarToConta(contaComandoCriarDTO)
                )
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}