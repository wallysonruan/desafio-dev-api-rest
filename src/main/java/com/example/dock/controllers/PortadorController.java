package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.services.PortadorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/portadores")
@AllArgsConstructor
public class PortadorController {

    private final PortadorService service;
    private final PortadorMapper mapper;


    @PostMapping
    public ResponseEntity<?> criarPortador(@RequestBody @Valid PortadorComandoCriarDto portadorComandoCriarDto){
        var notification = service.criarPortador(mapper.portadorComandoCriarDtoToPortador(portadorComandoCriarDto));

        if(notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.portadorToPortadorRespostaDto((Portador) notification.getResultado()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<?> deletarPortador(@PathVariable UUID uuid){
        var notification = service.deletarPortador(uuid);

        if(notification.hasErrors()){
            return new ResponseEntity( notification.getErrors(), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }
}
