package com.example.dock.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agencia")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @Column
    public String nome;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "agencia")
    @JsonIgnore
    public List<Conta> contas = new ArrayList<>();
}