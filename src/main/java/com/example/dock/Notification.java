package com.example.dock;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Notification<T> {
    private List<String> errors = new ArrayList<>();
    private T resultado;

    public void addError(String message){
        this.errors.add(message);
    }

    public boolean hasErrors(){
        return ! errors.isEmpty();
    }

    public String getErrors(){
        return errors.stream().collect(Collectors.joining(", "));
    }

    public void setResultado(T resultado){
        this.resultado = resultado;
    }

    public T getResultado(){
        return resultado;
    }
}
