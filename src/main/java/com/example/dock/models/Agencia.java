package com.example.dock.models;

import lombok.*;
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

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "agencia")
    public List<Conta> contas;
}
