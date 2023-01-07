package com.example.dock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "conta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Conta {

    @Id
    @GeneratedValue(generator = "uuid")
    public UUID uuid;

    @NaturalId
    @OneToOne
    @JoinColumn(name = "portador_id", referencedColumnName = "cpf")
    public Portador portador;

    @Column
    public BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "agencia_id", referencedColumnName = "uuid")
    public Agencia agencia;

    @Column
    public Boolean ativada;

    @Column
    public Boolean bloqueada;
}
