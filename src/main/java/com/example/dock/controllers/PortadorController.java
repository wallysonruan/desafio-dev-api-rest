package com.example.dock.controllers;

import com.example.dock.controllers.dtos.PortadorComandoCriarDto;
import com.example.dock.services.PortadorService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/portadores")
@NoArgsConstructor
public class PortadorController {

    private PortadorService service;
    private PortadorMapper mapper;

    public PortadorController(PortadorService portadorService, PortadorMapper mapper) {
        this.service = portadorService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> criarPortador(@RequestBody @Valid PortadorComandoCriarDto portadorComandoCriarDto){
        var response = service.criarPortador(mapper.portadorComandoCriarDtoToPortador(portadorComandoCriarDto));
        return new ResponseEntity<>(mapper.portadorToPortadorRespostaDto(response), HttpStatus.OK);
    }
}
