package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.models.Portador;
import com.example.dock.services.PortadorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/portadores")
@AllArgsConstructor
public class PortadorController {

    private final PortadorService service;
    private final PortadorMapper mapper;


    @PostMapping
    public ResponseEntity<?> criarPortador(@RequestBody @Valid PortadorComandoCriarDto portadorComandoCriarDto){
        var response = service.criarPortador(mapper.portadorComandoCriarDtoToPortador(portadorComandoCriarDto));

        if(response.hasErrors()){
            return new ResponseEntity<>(response.getErrors(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(mapper.portadorToPortadorRespostaDto((Portador) response.getResultado()), HttpStatus.OK);
    }
}
