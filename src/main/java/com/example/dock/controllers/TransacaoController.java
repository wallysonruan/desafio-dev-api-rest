package com.example.dock.controllers;

import com.example.dock.Notification;
import com.example.dock.controllers.dtos.TransacaoComandoCriarDto;
import com.example.dock.models.Transacao;
import com.example.dock.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    private final TransacaoService service;
    private final TransacaoMapper transacaoMapper;

    TransacaoController(TransacaoService service, TransacaoMapperImpl transacaoMapper){
        this.service = service;
        this.transacaoMapper = transacaoMapper;
    }


    @PostMapping
    public ResponseEntity<?> novaTransacao(@RequestBody @Valid TransacaoComandoCriarDto transacaoComandoCriarDto){
        var notification = service.novaTransacao(transacaoComandoCriarDto);

        if(notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>( transacaoMapper.transacaoToTransacaoDto(notification.getResultado()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<?> getTransactionsByPeriod(@PathVariable("uuid") UUID uuid, @RequestParam("initial-date") String initialDate, @RequestParam("final-date") String finalDate){
        Notification<List<Transacao>> notification = new Notification<>();
        LocalDate initialDateStringToLocalDate;
        LocalDate finalDateStringToLocalDate;

        try{
            initialDateStringToLocalDate = LocalDate.parse(initialDate);
            finalDateStringToLocalDate = LocalDate.parse(finalDate);
        }catch (DateTimeParseException exception){
            notification.addError("Data (s) inválida (s)! As datas devem seguir o formato: YYYY-MM-DD.");
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.BAD_REQUEST);
        }

        notification = service.getTransactionsByDate(uuid, LocalDate.parse(initialDate), LocalDate.parse(finalDate));

        if (notification.hasErrors()){
            return new ResponseEntity<>(notification.getErrors(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(transacaoMapper.listTransacaoToListTransacaoRespostaPorPeriodoDto(notification.getResultado()), HttpStatus.OK);
    }
}
