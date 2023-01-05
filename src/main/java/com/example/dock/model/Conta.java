package com.example.dock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "conta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Conta {

    @Id
    public UUID uuid;

    @NaturalId
    @OneToOne
    @JoinColumn(name = "portador_id", referencedColumnName = "cpf")
    public Portador portador;

    @Column
    public Long saldo;

    @Column
    @OneToMany
    @JoinColumn(name = "agencia_id")
    public Agencia agencia;

    @Column
    public Boolean ativada;

    @Column
    public Boolean bloqueada;
}
