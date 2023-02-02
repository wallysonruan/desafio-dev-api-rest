package com.example.dock.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "portador")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Portador {

    @Id
    @GeneratedValue(generator = "uuid")
    public UUID uuid;

    @NaturalId
    @CPF
    @Column(unique = true)
    public String cpf;

    @Column(name = "nome_completo")
    public String nomeCompleto;

    public void setCpf(String cpf){
        this.cpf = cpf.replaceAll("[^0-9]", "");
    }
}
