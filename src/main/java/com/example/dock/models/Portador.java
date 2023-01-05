package com.example.dock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "portador")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Portador {

    @Id
    @CPF
    public String cpf;

    @Column
    public String nome_completo;
}
