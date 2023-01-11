package com.example.dock.controllers;

import com.example.dock.controllers.dtos.ContaComandoCriarDTO;
import com.example.dock.models.Conta;
import com.example.dock.services.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("contas")
@AllArgsConstructor
public class ContaController {

    private ContaService service;
    private ContaMapper mapper;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody ContaComandoCriarDTO contaComandoCriarDTO){
        var notification = service.criarConta(contaComandoCriarDTO);

        if(notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(mapper.contaToContaRespostaDto((Conta) notification.getResultado()), HttpStatus.OK);
    }
}