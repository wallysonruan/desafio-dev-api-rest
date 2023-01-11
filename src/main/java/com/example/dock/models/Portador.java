package com.example.dock.models;

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
public class Portador {

    @Id
    @GeneratedValue(generator = "uuid")
    public UUID uuid;

    @NaturalId
    @CPF
    public String cpf;

    @Column
    public String nome_completo;

    public void setCpf(String cpf){
        this.cpf = cpf.replaceAll("[^0-9]", "");
    }
}
