package com.example.dock.controllers;

import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("transacao")
public class TransacaoController {
    private final TransacaoService service;

    TransacaoController(TransacaoService service){
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<?> novaTransacao(@RequestBody @Valid TransacaoComandoCriarDto transacaoComandoCriarDto){
        var response = service.novaTransacao(transacaoComandoCriarDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
