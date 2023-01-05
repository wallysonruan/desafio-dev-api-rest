package com.example.dock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "agencia")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Agencia {

    @Id
    public UUID uuid;

    @NaturalId
    public Long registro;

    @Column
    public String nome;

    @OneToMany(mappedBy = "agencia")
    public List<Conta> contas;
}
