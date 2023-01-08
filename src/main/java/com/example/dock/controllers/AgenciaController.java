package com.example.dock.controllers;

import com.example.dock.controllers.dtos.AgenciaComandoCriarDto;
import com.example.dock.models.Agencia;
import com.example.dock.services.AgenciaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/agencias")
@AllArgsConstructor
public class AgenciaController {

    private final AgenciaService service;
    private final AgenciaMapper mapper;

    @PostMapping
    public ResponseEntity<?> criarAgencia(@RequestBody @Valid AgenciaComandoCriarDto agenciaComandoCriarDto){
        var notification = service.criarAgencia(mapper.agenciaComandoCriarDtoToAgencia(agenciaComandoCriarDto));

        if(notification.hasErrors()){
            return new ResponseEntity(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(mapper.agenciaToAgenciaRespostaDto((Agencia) notification.getResultado()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletarAgencia(@PathVariable Long id){
        var notification = service.deletarAgencia(id);
        if(notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.noContent().build();
    }
}
