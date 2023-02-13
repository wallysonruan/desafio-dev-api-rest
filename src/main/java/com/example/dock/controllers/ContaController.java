package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.ContaComandoCriarDto;
import com.example.dock.models.Conta;
import com.example.dock.services.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("contas")
@AllArgsConstructor
public class ContaController {

    private ContaService service;
    private ContaMapper mapper;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody ContaComandoCriarDto contaComandoCriarDTO){
        var notification = service.criarConta(contaComandoCriarDTO);

        if(notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(mapper.contaToContaRespostaDto((Conta) notification.getResultado()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(service.getAll().getResultado(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConta(@PathVariable(value = "id") UUID contaUuid){
        Notification notification = service.deleteConta(contaUuid);

        if (notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.accepted().build();
    }
}